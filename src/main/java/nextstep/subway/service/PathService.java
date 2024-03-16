package nextstep.subway.service;

import nextstep.subway.controller.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.farecalculation.FareCalculation;
import nextstep.subway.domain.pathfinder.ShortestDistancePathFinder;
import nextstep.subway.domain.pathfinder.ShortestDurationPathFinder;
import nextstep.subway.domain.repository.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private LineRepository lineRepository;

    private StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target, PathType pathType, int age) {
        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationService.getStationById(source);
        Station targetStation = stationService.getStationById(target);

        Path path;
        if (PathType.DISTANCE == pathType) {
            ShortestDistancePathFinder pathFinder = new ShortestDistancePathFinder(lines);
            path = pathFinder.findPath(sourceStation, targetStation);
        } else {
            ShortestDurationPathFinder pathFinder = new ShortestDurationPathFinder(lines);
            path = pathFinder.findPath(sourceStation, targetStation);
        }

        int fare = FareCalculation.getFareCalculation(path.getDistance(), age);

        return PathResponse.from(path, fare);
    }
}
