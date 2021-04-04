package nextstep.subway.path.application;

import org.springframework.stereotype.Service;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.FarePoliciesCalculator;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class PathService {
    private final GraphService graphService;
    private final StationService stationService;

    public PathService(GraphService graphService, StationService stationService) {
        this.graphService = graphService;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target, PathType type) {
        SubwayGraph subwayGraph = graphService.findGraph(type);
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);
        Fare fare = FarePoliciesCalculator.calculate(pathResult);

        return PathResponse.of(pathResult, fare);
    }
}
