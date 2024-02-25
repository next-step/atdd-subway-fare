package nextstep.subway.service;

import nextstep.subway.controller.dto.PathResponse;
import nextstep.subway.controller.dto.StationResponse;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Paths;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
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

        Paths paths = new Paths(pathFinder.findPaths());
        List<Station> stations = paths.findShortestPath(source, target, PathType.of(pathType));
        long distance = paths.findShortestValue(source, target, PathType.DISTANCE);
        long duration = paths.findShortestValue(source, target, PathType.DURATION);

        return new PathResponse(StationResponse.listOf(stations), distance, duration);
    }

}
