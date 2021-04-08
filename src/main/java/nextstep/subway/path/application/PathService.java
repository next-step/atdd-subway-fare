package nextstep.subway.path.application;

import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.dto.PathParams;
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

    public PathResponse findPath(PathParams pathParams) {
        SubwayGraph subwayGraph = graphService.findGraph(pathParams.getType());
        Station sourceStation = stationService.findStationById(pathParams.getSource());
        Station targetStation = stationService.findStationById(pathParams.getTarget());
        PathResult pathResult = subwayGraph.createPathResult(sourceStation, targetStation, pathParams.getLoginMember());
        return PathResponse.of(pathResult);
    }
}
