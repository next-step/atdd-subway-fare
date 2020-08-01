package nextstep.subway.maps.fare.application;

import nextstep.subway.maps.fare.domain.Fare;
import nextstep.subway.maps.fare.domain.FareContext;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.map.application.PathService;
import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.domain.SubwayPath;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FareService {
    private final PathService pathService;
    private final FareCalculator fareCalculator;

    public FareService(PathService pathService, FareCalculator fareCalculator) {
        this.pathService = pathService;
        this.fareCalculator = fareCalculator;
    }

    public Fare calculateFare(List<Line> lines, Long source, Long target, Integer distance, PathType type) {
        if (type != PathType.DISTANCE) {
            SubwayPath pathForCalculate = pathService.findPath(lines, source, target, PathType.DISTANCE);
            FareContext fareContext = new FareContext(pathForCalculate.calculateDistance());
            return fareCalculator.calculate(fareContext);
        }

        FareContext fareContext = new FareContext(distance);
        return fareCalculator.calculate(fareContext);
    }

    public Fare calculateFare(List<Line> lines, List<LineStationEdge> lineStationEdges, PathType type) {
        return null;
    }
}