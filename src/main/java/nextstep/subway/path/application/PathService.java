package nextstep.subway.path.application;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.dto.FarePolicyRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

        int fare = getFare(path);
        return PathResponse.of(path, fare);
    }

    private int getFare(Path path) {
        int distance = path.extractDistance();

        List<FarePolicy> farePolicies = Arrays.asList(
                DistancePolicy.choicePolicyByDistance(distance),
                LinePolicy.from(path.getSections())
        );
        FareCalculator fareCalculator = FareCalculator.from(farePolicies);

        FarePolicyRequest farePolicyRequest = FarePolicyRequest.builder()
                .distance(distance)
                .build();

        return fareCalculator.calculate(farePolicyRequest);
    }
}
