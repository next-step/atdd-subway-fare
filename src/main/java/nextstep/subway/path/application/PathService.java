package nextstep.subway.path.application;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.dto.CostRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private GraphService graphService;
    private StationService stationService;
    private PaymentPolicyHandler paymentPolicyHandler;

    public PathService(GraphService graphService, StationService stationService,  PaymentPolicyHandler paymentPolicyHandler) {
        this.graphService = graphService;
        this.stationService = stationService;
        this.paymentPolicyHandler = paymentPolicyHandler;
    }

    public PathResponse findPath(LoginMember loginMember, Long source, Long target, PathType type) {
        SubwayGraph subwayGraph = graphService.findGraph(type);
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);
        Cost cost = calculateCost(pathResult, loginMember.getAge());

        return PathResponse.of(pathResult, cost);
    }

    private Cost calculateCost(PathResult pathResult, Integer age) {
        CostRequest costRequest = CostRequest.of(pathResult, age);
        CostRequest result = paymentPolicyHandler.execute(costRequest);

        return result.getCost();
    }
}
