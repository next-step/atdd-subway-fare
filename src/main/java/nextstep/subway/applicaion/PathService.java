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

    public PathResponse findPath(Long source, Long target, PathType type, int age) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines, type);
        Path path = subwayMap.findPath(upStation, downStation);
        AgeFare ageFare = AgeFare.findAgeFareType(age);

        if (PathType.DURATION == type) {
            // 최단 시간의 거리가 아닌 최단 경로의 거리 구하기
            Path shortestPath = new SubwayMap(lines, PathType.DISTANCE).findPath(upStation, downStation);
            return PathResponse.of(path, new PathFare(shortestPath, ageFare));
        }

        return PathResponse.of(path, new PathFare(path, ageFare));
    }
}
