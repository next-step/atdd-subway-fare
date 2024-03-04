package nextstep.subway.service;

import nextstep.subway.dto.path.PathResponse;
import nextstep.subway.dto.path.PathType;
import nextstep.subway.entity.Sections;
import nextstep.subway.entity.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    /** 경로 조회 */
    public PathResponse getPaths(Long source, Long target, PathType type) {
        Station sourceStation = stationService.findStation(source);
        Station targetStation = stationService.findStation(target);
        List<Sections> sectionsList = lineService.findSectionsList();

        PathFinder pathFinder = createPathFinder(type, sectionsList);
        return pathFinder.getShortestPath(sourceStation, targetStation);
    }

    private PathFinder createPathFinder(PathType type, List<Sections> sectionsList) {
        return type == PathType.DISTANCE ?
            new DistancePathFinder(sectionsList) : new DurationPathFinder(sectionsList);
    }
}
