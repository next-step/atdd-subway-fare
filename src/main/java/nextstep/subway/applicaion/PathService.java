package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathCondition;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(Long source, Long target, PathCondition pathCondition) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);

        Path path = subwayMap.findPath(upStation, downStation, pathCondition.getEdgeInitiator());
        int shortestDistance = shortestDistance(pathCondition, upStation, downStation, lines, path);
        System.out.println("shortestDistance = " + shortestDistance);
        int fare = path.calculateFare(shortestDistance);

        return PathResponse.of(path, fare);
    }

    private int shortestDistance(PathCondition pathType, Station upStation, Station downStation, List<Line> lines, Path path) {
        if (pathType == PathCondition.DURATION) {
            Path shortestPath = findShortestPath(lines, upStation, downStation);
            return shortestPath.extractDistance();
        }
        return path.extractDistance();
    }

    private Path findShortestPath(List<Line> lines, Station upStation, Station downStation) {
        SubwayMap subwayMap = new SubwayMap(lines);
        return subwayMap.findPath(upStation, downStation, PathCondition.DISTANCE.getEdgeInitiator());
    }
}
