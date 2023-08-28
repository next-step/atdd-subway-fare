package nextstep.subway.service;

import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.ShortestPathFinder;
import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.entity.LineRepository;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.entity.StationRepository;
import nextstep.subway.dto.PathResponse;
import nextstep.subway.dto.StationResponse;
import nextstep.subway.exception.StationNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse getShortestPath(long sourceStationId, long targetStationId) {
        Station sourceStation = getStation(sourceStationId);
        Station targetStation = getStation(targetStationId);

        List<Line> lineList = lineRepository.findAll();

        PathFinder pathFinder = ShortestPathFinder.of(lineList, sourceStation, targetStation);
        List<Station> searchedPath = pathFinder.getPath();
        BigInteger weight = pathFinder.getWeight();
        return new PathResponse(searchedPath.stream().map(StationResponse::from).collect(Collectors.toList()), weight);
    }

    private Station getStation(long sourceStationId) {
        return stationRepository.findById(sourceStationId)
                .orElseThrow(() -> new StationNotFoundException("station.0001"));
    }
}
