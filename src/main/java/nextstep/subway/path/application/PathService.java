package nextstep.subway.path.application;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.domain.policy.distance.DistancePolicyFactory;
import nextstep.subway.path.domain.policy.FarePolicy;
import nextstep.subway.path.domain.policy.line.LinePolicyFactory;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private GraphService graphService;
    private StationService stationService;

    public PathService(GraphService graphService, StationService stationService) {
        this.graphService = graphService;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target, PathType type) {
        SubwayGraph subwayGraph = graphService.findGraph(type);
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);

        Fare fare = getFare(pathResult);
        return PathResponse.of(pathResult, fare.getFareValue());
    }

    private Fare getFare(PathResult pathResult) {
        FarePolicy distancePolicy = DistancePolicyFactory.findPolicy(pathResult.getTotalDistance());
        FarePolicy linePolicy = LinePolicyFactory.findPolicy(pathResult.getMaxLineFare());
        return Fare.calculate(distancePolicy, linePolicy);
    }
}
