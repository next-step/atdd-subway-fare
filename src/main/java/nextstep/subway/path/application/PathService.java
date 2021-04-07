package nextstep.subway.path.application;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.domain.SubwayPathTime;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
            return getPathResponseByArrivalTime(subwayGraph, sourceStation, targetStation, age, datetime);
        }

        PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);
        SubwayPathTime subwayPathTime = new SubwayPathTime(pathResult);
        LocalDateTime arriveTime = subwayPathTime.getArriveTime(datetime);

        return PathResponse.of(pathResult, age, arriveTime);
    }

    // TODO: 도메인 로직으로 리팩토링 필요
    private PathResponse getPathResponseByArrivalTime(SubwayGraph subwayGraph, Station sourceStation, Station targetStation, int age, LocalDateTime datetime) {
        List<PathResult> pathResults = subwayGraph.findAllPath(sourceStation, targetStation);

        PathResult fastPathResult = null;
        LocalDateTime fastArriveTime = LocalDateTime.MAX;
        for (PathResult pathResult : pathResults) {
            SubwayPathTime subwayPathTime = new SubwayPathTime(pathResult);
            LocalDateTime arriveTime = subwayPathTime.getArriveTime(datetime);

            if (arriveTime.isBefore(fastArriveTime)) {
                fastPathResult = pathResult;
                fastArriveTime = arriveTime;
            }
        }

        return PathResponse.of(fastPathResult, age, fastArriveTime);
    }
}
