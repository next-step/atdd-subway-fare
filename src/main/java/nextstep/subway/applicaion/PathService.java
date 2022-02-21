package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
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

    public PathResponse findPath(Long source, Long target, PathType type) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines, type);
        Path path = subwayMap.findPath(upStation, downStation);

        if (PathType.DURATION == type) {
            // 최단 시간의 거리가 아닌 최단 경로의 거리 구하기
            Path shortestDistancePath = new SubwayMap(lines, PathType.DISTANCE).findPath(upStation, downStation);
            path = path.changeShortestDistancePath(shortestDistancePath);
        }

        return PathResponse.of(path);
    }
}
