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

    public PathResponse findPath(Long source, Long target, String type) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, PathType.valueOf(type));

        if (type.equals(PathType.DISTANCE.name())) {
            return caculateFare(path);
        }

        return PathResponse.of(path, 0);
    }

    public PathResponse caculateFare(Path path) {
        SubwayFare subwayFare = new SubwayFare();
        int totalFare = subwayFare.calculateFare(path.extractDistance());
        return PathResponse.of(path, totalFare);
    }
}
