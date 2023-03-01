package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import nextstep.subway.domain.policy.CalculateConditions;
import nextstep.subway.domain.policy.FarePolicies;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final FarePolicies farePolicies = new FarePolicies();

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target, PathRequestType type, int age) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);

        Path path = subwayMap.findPath(upStation, downStation, type);
        int fare = calculateFare(path, age);
        return PathResponse.of(path, fare);

    }

    private int calculateFare(Path path, int age) {
        CalculateConditions conditions = CalculateConditions.builder(path.extractDistance())
                .age(age)
                .lines(path.getLines())
                .build();

        return this.farePolicies.calculate(conditions);


    }
}
