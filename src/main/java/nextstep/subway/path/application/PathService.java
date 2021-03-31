package nextstep.subway.path.application;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.Discount;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.domain.policy.DiscountPolicy;
import nextstep.subway.path.domain.policy.discount.DiscountPolicyFactory;
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

    public PathResponse findPath(Long source, Long target, PathType type, LoginMember loginMember) {
        SubwayGraph subwayGraph = graphService.findGraph(type);
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);

        int fare = getFare(pathResult);
        fare = excuteDiscount(loginMember, fare);
        return PathResponse.of(pathResult, fare);
    }

    private int getFare(PathResult pathResult) {
        FarePolicy distancePolicy = DistancePolicyFactory.findPolicy(pathResult.getTotalDistance());
        FarePolicy linePolicy = LinePolicyFactory.findPolicy(pathResult.getMaxLineFare());
        return Fare.calculate(distancePolicy, linePolicy).getFareValue();
    }

    private int excuteDiscount(LoginMember loginMember, int fare) {
        DiscountPolicy discountPolicy = DiscountPolicyFactory.findPolicy(loginMember.getAge());
        fare = Discount.excute(fare, discountPolicy);
        return fare;
    }
}
