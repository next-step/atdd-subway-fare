package nextstep.subway.maps.map.application;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.map.domain.*;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {

    public static final int MAX_PATH_COUNT = 10000;

    public SubwayPath findPath(List<Line> lines, Long source, Long target, PathType type, LocalDateTime time) {
        if (type == PathType.ARRIVAL_TIME) {
            return findPathByArrivalTime(lines, source, target, time);
        }

        SubwayGraph graph = createSubwayGraph(lines, type);

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Long, LineStationEdge> path = dijkstraShortestPath.getPath(source, target);

        return convertSubwayPath(path);
    }

    public SubwayPath findPathByArrivalTime(List<Line> lines, Long source, Long target, LocalDateTime departTime) {
        SubwayGraph graph = createSubwayGraph(lines, PathType.ARRIVAL_TIME);

        List<GraphPath<Long, LineStationEdge>> paths = getAllPaths(source, target, graph);
        List<SubwayPath> subwayPaths = paths.stream()
                .map(this::convertSubwayPath)
                .collect(Collectors.toList());
        TimePaths timePaths = TimePaths.of(subwayPaths);
        TimePath fastestArrivalPath = timePaths.findFastestArrivalPath(departTime);

        return fastestArrivalPath.getPath();
    }

    private SubwayGraph createSubwayGraph(List<Line> lines, PathType type) {
        SubwayGraph graph = new SubwayGraph(LineStationEdge.class);
        graph.addVertexWith(lines);
        graph.addEdge(lines, type);
        return graph;
    }


    private SubwayPath convertSubwayPath(GraphPath graphPath) {
        return new SubwayPath((List<LineStationEdge>) graphPath.getEdgeList().stream().collect(Collectors.toList()));
    }


    private List<GraphPath<Long, LineStationEdge>> getAllPaths(Long source, Long target, SubwayGraph graph) {
        KShortestPaths<Long, LineStationEdge> kShortestPaths = new KShortestPaths<>(graph, MAX_PATH_COUNT);
        return kShortestPaths.getPaths(source, target);
    }
}
