package nextstep.subway.path.application;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
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

    public PathResult findPath(Long source, Long target, PathType pathType) {
        SubwayGraph subwayGraph = graphService.findGraph(pathType);
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        return subwayGraph.findPath(sourceStation, targetStation);
    }
}
