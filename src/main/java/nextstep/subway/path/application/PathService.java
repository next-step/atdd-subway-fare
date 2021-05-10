package nextstep.subway.path.application;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.Fare;
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
    private FareService fareService;

    public PathService(GraphService graphService, StationService stationService, FareService fareService) {
        this.graphService = graphService;
        this.stationService = stationService;
        this.fareService = fareService;
    }

    public PathResponse findPath(Long source, Long target, PathType type, LoginMember loginMember) {
        SubwayGraph subwayGraph = graphService.findGraph(type);
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);

        int cost = fareService.calculate(pathResult.getMaxAdditionalFee(), pathResult.getTotalDistance(), loginMember);
        return PathResponse.of(pathResult, cost);
    }
}
