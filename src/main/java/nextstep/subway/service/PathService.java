package nextstep.subway.service;

import nextstep.subway.controller.dto.PathResponse;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.StationRepository;
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

    public PathResponse findPaths(Long sourceId, Long targetId, String pathType) {
        Station source = stationRepository.getBy(sourceId);
        Station target = stationRepository.getBy(targetId);

        Path path = pathFinder.findPath(pathType);
        List<Station> stations = path.findShortestPath(source, target);
        int value = path.findShortestValue(source, target);
        return new PathResponse();
    }

}
