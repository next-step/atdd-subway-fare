package nextstep.subway.path.application;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.path.domain.FastPathResult;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.domain.SubwayPathTime;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class PathService {

    private final GraphService graphService;
    private final StationService stationService;

    public PathService(GraphService graphService, StationService stationService) {
        this.graphService = graphService;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target, PathType type, int age, LocalDateTime datetime) {
        SubwayGraph subwayGraph = graphService.findGraph(type);
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);

        if (type == PathType.ARRIVAL_TIME) {
            List<PathResult> pathResults = subwayGraph.findAllPath(sourceStation, targetStation);
            SubwayPathTime subwayPathTime = new SubwayPathTime(pathResults);
            FastPathResult fastPathResult = subwayPathTime.getFastPathResult(datetime);

            return PathResponse.of(fastPathResult.getPathResult(), age, fastPathResult.getArriveTime());
        }

        PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);
        SubwayPathTime subwayPathTime = new SubwayPathTime(Collections.singletonList(pathResult));
        LocalDateTime arriveTime = subwayPathTime.getArriveTime(pathResult, datetime);

        return PathResponse.of(pathResult, age, arriveTime);
    }
}
