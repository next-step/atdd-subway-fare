package nextstep.subway.path.application;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private GraphService graphService;
    private StationService stationService;
    private FareCalculator fareCalculator;

    public PathService(GraphService graphService, StationService stationService, FareCalculator fareCalculator) {
        this.graphService = graphService;
        this.stationService = stationService;
        this.fareCalculator = fareCalculator;
    }

    public PathResponse findPath(LoginMember loginMember, Long source, Long target, PathType type) {
        SubwayGraph subwayGraph = graphService.findGraph(type);
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);
        int totalFare = fareCalculator.getTotalFare(pathResult.getGoThroughLine(), pathResult.getTotalDistance(), loginMember.getAge());
        return PathResponse.of(pathResult,totalFare);
    }
}
