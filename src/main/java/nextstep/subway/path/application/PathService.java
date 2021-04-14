package nextstep.subway.path.application;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.domain.enumeration.FareType;
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

    public PathResponse findPath(Long source, Long target, PathType type) {
        SubwayGraph subwayGraph = graphService.findGraph(type);
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);

        return PathResponse.of(pathResult, calculateFare(pathResult.getTotalDistance()));
    }

    public int calculateFare(int distance) {
        if (distance > 10 && distance <= 50) {
            return FareType.OVER_10_KM_BELOW_50_KM.calucate(distance);
        }

        if (distance > 50) {
            return FareType.OVER_50_KM.calucate(distance);
        }

        return FareType.DEFAULT.calucate(distance);
    }
}
