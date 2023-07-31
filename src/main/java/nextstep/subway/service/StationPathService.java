package nextstep.subway.service;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.*;
import nextstep.subway.domain.service.ShortestStationPath;
import nextstep.subway.domain.service.StationPathAccumulateService;
import nextstep.subway.domain.service.StationPathSearchRequestType;
import nextstep.subway.domain.service.StationShortestPathCalculateService;
import nextstep.subway.exception.StationLineSearchFailException;
import nextstep.subway.service.dto.StationPathResponse;
import nextstep.subway.service.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationPathService {
    private final StationLineRepository stationLineRepository;
    private final StationRepository stationRepository;
    private final StationShortestPathCalculateService stationShortestPathCalculateService;
    private final StationPathAccumulateService stationPathAccumulateService;

    @Deprecated
    @Transactional(readOnly = true)
    public StationPathResponse searchStationPath(Long startStationId, Long destinationStationId) {
        checkValidPathFindRequest(startStationId, destinationStationId);

        final List<Station> totalStations = stationRepository.findAll();
        final List<StationLineSection> totalStationLineSections = stationLineRepository.findAll()
                .stream()
                .map(StationLine::getSections)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        final Station startStation = stationRepository.findById(startStationId)
                .orElseThrow(() -> new StationLineSearchFailException("there is no start station"));
        final Station destinationStation = stationRepository.findById(destinationStationId)
                .orElseThrow(() -> new StationLineSearchFailException("there is no destination station"));

        final ShortestStationPath path = stationShortestPathCalculateService.calculateShortestPath(startStation, destinationStation, totalStationLineSections);

        final Map<Long, Station> stationMap = totalStations.stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));

        final List<Long> pathStationIds = path.getStationIds();

        return StationPathResponse.builder()
                .stations(pathStationIds.stream()
                        .map(stationMap::get)
                        .map(StationResponse::fromEntity)
                        .collect(Collectors.toList()))
                .distance(path.getDistance())
                .build();
    }

    @Transactional(readOnly = true)
    public StationPathResponse searchStationPath(Long startStationId, Long destinationStationId, StationPathSearchRequestType type) {
        checkValidPathFindRequest(startStationId, destinationStationId);

        final List<Station> totalStations = stationRepository.findAll();
        final Map<Long, Station> totalStationMap = totalStations.stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));

        final Station startStation = stationRepository.findById(startStationId)
                .orElseThrow(() -> new StationLineSearchFailException("there is no start station"));
        final Station destinationStation = stationRepository.findById(destinationStationId)
                .orElseThrow(() -> new StationLineSearchFailException("there is no destination station"));

        final List<Long> pathStationIds = stationShortestPathCalculateService.getShortestPathStations(startStation, destinationStation, type);
        final BigDecimal distance = stationPathAccumulateService.accumulateTotalDistance(pathStationIds);
        final Long duration = stationPathAccumulateService.accumulateTotalDuration(pathStationIds);

        return StationPathResponse.builder()
                .stations(pathStationIds.stream()
                        .map(totalStationMap::get)
                        .map(StationResponse::fromEntity)
                        .collect(Collectors.toList()))
                .distance(distance)
                .duration(duration)
                .build();
    }

    private void checkValidPathFindRequest(Long startStationId, Long destinationStationId) {
        if (startStationId.equals(destinationStationId)) {
            throw new StationLineSearchFailException("start station and destination station are equals");
        }
    }
}
