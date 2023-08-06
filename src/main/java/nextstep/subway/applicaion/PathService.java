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

        int fare = new FareCalculator(path.extractDistance()).fare();

        if (type != PathType.DISTANCE) {
            fare = findFareByDistance(upStation, downStation, lines);
        }

        return PathResponse.of(path, fare);
    }

    private static int findFareByDistance(Station upStation, Station downStation, List<Line> lines) {
        Path distancePath = new SubwayMap(lines, PathType.DISTANCE).findPath(upStation, downStation);
        return new FareCalculator(distancePath.extractDistance()).fare();
    }
}
