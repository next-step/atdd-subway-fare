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

    // TODO: make test and refactoring
    public PathResponse findPath(Long source, Long target, PathType type) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines, type);
        Path path = subwayMap.findPath(upStation, downStation);

        if (type != PathType.DISTANCE) {
            SubwayMap subwayMapForFare = new SubwayMap(lines, type);
            Path pathForFare = subwayMapForFare.findPath(upStation, downStation);
            FareCalculator fareCalculator = new FareCalculator(pathForFare);
            int fare = fareCalculator.fare();
            return PathResponse.of(path, fare);
        }

        FareCalculator fareCalculator = new FareCalculator(path);
        int fare = fareCalculator.fare();
        return PathResponse.of(path, fare);
    }
}
