package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target, PathType pathType) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();

        Path path = findPath(lines, pathType, upStation, downStation);
        int shortestDistance = shortestDistance(pathType, upStation, downStation, lines, path);

        return PathResponse.of(path, FareType.fare(shortestDistance));
    }

    private int shortestDistance(PathType pathType, Station upStation, Station downStation, List<Line> lines, Path path) {
        if (pathType == PathType.DURATION) {
            Path shortestPath = findPath(lines, PathType.DISTANCE, upStation, downStation);
            return shortestPath.extractDistance();
        }
        return path.extractDistance();
    }

    private Path findPath(List<Line> lines, PathType pathType, Station upStation, Station downStation) {
        SubwayMap subwayMap = new SubwayMap(lines, pathType);
        return subwayMap.findPath(upStation, downStation);
    }
}
