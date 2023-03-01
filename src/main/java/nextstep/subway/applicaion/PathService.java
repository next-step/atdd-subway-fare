package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import nextstep.subway.domain.policy.calculate.CalculateConditions;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final FareCalculatorService fareCalculatorService;

    public PathService(LineService lineService, StationService stationService, FareCalculatorService fareCalculatorService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.fareCalculatorService = fareCalculatorService;
    }

    public PathResponse findPath(Long source, Long target, PathRequestType type, int age) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);

        Path path = subwayMap.findPath(upStation, downStation, type);
        int fare = fareCalculatorService.calculateFare(setConditions(path, age));
        return PathResponse.of(path, fare);

    }

    private CalculateConditions setConditions(Path path, int age) {
        return CalculateConditions.builder(path.extractDistance())
                .age(age)
                .lines(path.getLines())
                .build();
    }


}
