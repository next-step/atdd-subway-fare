package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.fare.DistancePolicy;
import nextstep.subway.domain.fare.Fare;
import nextstep.subway.domain.fare.FarePolicy;
import nextstep.subway.domain.fare.LinePolicy;
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

    public PathResponse findPath(Long source, Long target, PathType pathType) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines, pathType);
        Path path = subwayMap.findPath(upStation, downStation);
        Fare fare = findFare(path);

        return PathResponse.of(path, fare);
    }

    private Fare findFare(Path path) {
        List<FarePolicy> farePolicies = Arrays.asList(
                DistancePolicy.from(path.extractDistance()),
                LinePolicy.from(path.extractExpensiveExtraCharge())
        );

        Fare fare = Fare.standard();
        for(FarePolicy policy : farePolicies) {
            policy.calculate(fare);
        }
        return fare;
    }
}
