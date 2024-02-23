package nextstep.subway.service;

import nextstep.subway.controller.dto.PathResponse;
import nextstep.subway.controller.dto.StationResponse;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.StationRepository;
import nextstep.subway.strategy.PathType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final PathFinder pathFinder;

    public PathService(StationRepository stationRepository, PathFinder pathFinder) {
        this.stationRepository = stationRepository;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPaths(Long sourceId, Long targetId, PathType pathType) {
        Station source = stationRepository.getBy(sourceId);
        Station target = stationRepository.getBy(targetId);

        Path path = pathFinder.findPath(pathType);
        List<Station> stations = path.findShortestPath(source, target);
        int value = path.findShortestValue(source, target);
        return pathType.getResponse().apply(stations, value);
    }

}
