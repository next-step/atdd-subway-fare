package nextstep.subway.path.application;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.domain.policy.PolicyApplier;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private GraphService graphService;
    private StationService stationService;
    private PolicyApplier policyApplier;

    public PathService(GraphService graphService, StationService stationService, PolicyApplier policyApplier) {
        this.graphService = graphService;
        this.stationService = stationService;
        this.policyApplier = policyApplier;
    }

    public PathResponse findPath(LoginMember loginMember, Long source, Long target, PathType type) {
        SubwayGraph subwayGraph = graphService.findGraph(type);
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);
        int fare = policyApplier.applyFarePolicy(pathResult, loginMember).getFare();
        return PathResponse.of(pathResult, fare);
    }
}
