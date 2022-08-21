package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;
    private final FareService fareService;

    public PathService(LineService lineService, StationService stationService, FareService fareService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.fareService = fareService;
    }

    public PathResponse findPath(Long source, Long target, PathType type) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);

        Path shortestPath = subwayMap.findPath(upStation, downStation, PathType.DISTANCE);
        int fare = fareService.calculate(shortestPath);

        if (PathType.DURATION.equals(type)) {
            Path path = subwayMap.findPath(upStation, downStation, type);
            return PathResponse.of(path, fare);
        }

        return PathResponse.of(shortestPath, fare);
    }
}
