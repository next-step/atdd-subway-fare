package nextstep.subway.path.application;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.path.domain.Cost;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.PaymentPolicy;
import nextstep.subway.path.domain.SubwayGraph;
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

    public PathResponse findPath(Long source, Long target, PathType type) {
        SubwayGraph subwayGraph = graphService.findGraph(type);
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);
        Cost cost = paymentPolicy.cost(pathResult);
        return PathResponse.of(pathResult, cost);
    }
}
