package nextstep.subway.path.application;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.domain.policy.AgeDiscountPolicy;
import nextstep.subway.path.domain.policy.DistancePolicy;
import nextstep.subway.path.domain.policy.FarePolicy;
import nextstep.subway.path.domain.policy.LinePolicy;
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

        Fare fare = new Fare();

        FarePolicy fareDistancePolicy = DistancePolicy.getDistancePolicy(pathResult.getTotalDistance());
        FarePolicy linePolicy = LinePolicy.getLinePolicy(pathResult.getMaxFare());
        FarePolicy discountPolicy = AgeDiscountPolicy.getAgeDiscountPolicy(loginMember.getAge());

        fare.addAllFarePolicy(fareDistancePolicy, linePolicy, discountPolicy);
        fare.calculate(fare.getFare());
        return PathResponse.of(pathResult, fare);
    }

}
