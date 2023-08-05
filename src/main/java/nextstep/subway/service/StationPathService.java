package nextstep.subway.service;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.domain.service.StationFeeCalculateService;
import nextstep.subway.domain.service.StationPathAccumulateService;
import nextstep.subway.domain.service.StationPathSearchRequestType;
import nextstep.subway.domain.service.StationShortestPathCalculateService;
import nextstep.subway.exception.StationLineSearchFailException;
import nextstep.subway.service.dto.StationPathResponse;
import nextstep.subway.service.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationPathService {
    private final StationRepository stationRepository;
    private final StationShortestPathCalculateService stationShortestPathCalculateService;
    private final StationPathAccumulateService stationPathAccumulateService;
    private final StationFeeCalculateService stationFeeCalculateService;

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
        final BigDecimal fee = stationFeeCalculateService.calculateFee(distance);
        final Long duration = stationPathAccumulateService.accumulateTotalDuration(pathStationIds);

        return StationPathResponse.builder()
                .stations(pathStationIds.stream()
                        .map(totalStationMap::get)
                        .map(StationResponse::fromEntity)
                        .collect(Collectors.toList()))
                .distance(distance)
                .fee(fee)
                .duration(duration)
                .build();
    }

    private void checkValidPathFindRequest(Long startStationId, Long destinationStationId) {
        if (startStationId.equals(destinationStationId)) {
            throw new StationLineSearchFailException("start station and destination station are equals");
        }
    }
}
