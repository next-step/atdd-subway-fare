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

    public PathResponse findPath(Long source, Long target) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayDistanceMap(lines);
        Path path = subwayMap.findPath(upStation, downStation);
        FareCalculator fareCalculator = new FareDistanceStrategy();
        Fare fare = fareCalculator.calculate2(path.extractDistance());
//        Fare fare = Fare.of(new FareDistanceStrategy(), path.extractDistance());

        return PathResponse.of(path, fare);
    }
}
