package nextstep.subway.service;

import nextstep.subway.controller.dto.PathResponse;
import nextstep.subway.domain.*;
import nextstep.subway.domain.pathfinder.ShortestDistancePathFinder;
import nextstep.subway.domain.pathfinder.ShortestDurationPathFinder;
import nextstep.subway.domain.repository.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.subway.controller.dto.StationResponse.stationsToStationResponses;

@Service
@Transactional(readOnly = true)
public class PathService {
    private LineRepository lineRepository;

    private StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target, PathType pathType) {
        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationService.getStationById(source);
        Station targetStation = stationService.getStationById(target);

        if (PathType.DISTANCE == pathType) {
            ShortestDistancePathFinder pathFinder = new ShortestDistancePathFinder(lines);
            Path path = pathFinder.findPath(sourceStation, targetStation);
            return PathResponse.from(path);
        }
        ShortestDurationPathFinder pathFinder = new ShortestDurationPathFinder(lines);
        Path path = pathFinder.findPath(sourceStation, targetStation);
        return PathResponse.from(path);
    }
}
