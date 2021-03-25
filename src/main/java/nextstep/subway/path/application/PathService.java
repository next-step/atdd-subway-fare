package nextstep.subway.path.application;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final GraphService graphService;
    private final StationService stationService;
    private final FareCalculator adultFareCalculator;

    public PathService(GraphService graphService, StationService stationService, FareCalculator adultFareCalculator) {
        this.graphService = graphService;
        this.stationService = stationService;
        this.adultFareCalculator = adultFareCalculator;
    }

    public PathResponse findPath(Long source, Long target, PathType type) {
        SubwayGraph subwayGraph = graphService.findGraph(type);
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);
        return PathResponse.of(pathResult, adultFareCalculator);
    }
}
