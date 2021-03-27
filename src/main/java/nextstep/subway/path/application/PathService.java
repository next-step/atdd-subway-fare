package nextstep.subway.path.application;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private GraphService graphService;
    private StationService stationService;
    private PaymentPolicy paymentPolicy;

    public PathService(GraphService graphService, StationService stationService, PaymentPolicy paymentPolicy) {
        this.graphService = graphService;
        this.stationService = stationService;
        this.paymentPolicy = paymentPolicy;
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
        Cost cost = paymentPolicy.cost(pathResult);
        return CostByAge.applyDiscount(cost, age);
    }
}
