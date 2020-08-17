package nextstep.subway.maps.map.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.domain.SubwayGraph;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.maps.map.domain.SubwayPaths;

@Service
public class PathService {

    public static final int MAX_PATH_COUNT = 10000;

    public SubwayPath findPath(List<Line> lines, Long source, Long target, PathType type, LocalDateTime time) {
        if (type == PathType.ARRIVAL) {
            return findPathByArrivalTime(lines, source, target, time);
        }

        SubwayGraph graph = createSubwayGraph(lines, type);
        DijkstraShortestPath<Long, LineStationEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Long, LineStationEdge> path = dijkstraShortestPath.getPath(source, target);

        return convertSubwayPath(path);
    }

    protected SubwayPath findPathByArrivalTime(List<Line> lines, Long source, Long target,
        LocalDateTime departureTime) {
        SubwayGraph graph = createSubwayGraph(lines, PathType.ARRIVAL);
        List<GraphPath<Long, LineStationEdge>> paths = getAllPaths(source, target, graph);
        List<SubwayPath> subwayPaths = paths.stream()
            .map(this::convertSubwayPath)
            .collect(Collectors.toList());
        SubwayPaths timePaths = SubwayPaths.of(subwayPaths);
        return timePaths.findFastestArrivalPath(departureTime);
    }

    private List<GraphPath<Long, LineStationEdge>> getAllPaths(Long source, Long target, SubwayGraph graph) {
        KShortestPaths<Long, LineStationEdge> kShortestPaths = new KShortestPaths<>(graph, MAX_PATH_COUNT);
        return kShortestPaths.getPaths(source, target);
    }

    public SubwayPath findPath(List<Line> lines, Long source, Long target, PathType type) {
        SubwayGraph graph = createSubwayGraph(lines, type);
        graph.addVertexWith(lines);
        graph.addEdge(lines, type);

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Long, LineStationEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Long, LineStationEdge> path = dijkstraShortestPath.getPath(source, target);

        return convertSubwayPath(path);
    }

    private SubwayGraph createSubwayGraph(List<Line> lines, PathType type) {
        SubwayGraph graph = new SubwayGraph(LineStationEdge.class);
        graph.addVertexWith(lines);
        graph.addEdge(lines, type);
        return graph;
    }

    private SubwayPath convertSubwayPath(GraphPath<Long, LineStationEdge> graphPath) {
        return new SubwayPath(Lists.newArrayList(graphPath.getEdgeList()));
    }
}
