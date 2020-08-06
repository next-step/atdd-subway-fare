package nextstep.subway.maps.map.application;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.map.domain.LineStationEdge;
import nextstep.subway.maps.map.domain.PathType;
import nextstep.subway.maps.map.domain.SubwayGraph;
import nextstep.subway.maps.map.domain.SubwayPath;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public SubwayPath findPath(List<Line> lines, Long source, Long target, PathType type) {
        SubwayGraph graph = new SubwayGraph(LineStationEdge.class);
        graph.addVertexWith(lines);
        graph.addEdge(lines, type);

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Long, LineStationEdge> path = dijkstraShortestPath.getPath(source, target);

        return convertSubwayPath(path);
    }

    public List<SubwayPath> findAllPath(List<Line> lines, Long source, Long target) {
        SubwayGraph graph = new SubwayGraph(LineStationEdge.class);
        graph.addVertexWith(lines);
        graph.addEdge(lines);
        List<GraphPath<Long, LineStationEdge>> paths = new KShortestPaths<>(graph, 1000).getPaths(source, target);
        return paths.stream().map(this::convertSubwayPath).collect(Collectors.toList());
    }

    @SuppressWarnings({"unchecked", "SimplifyStreamApiCallChains", "rawtypes"})
    private SubwayPath convertSubwayPath(GraphPath graphPath) {
        return new SubwayPath((List<LineStationEdge>) graphPath.getEdgeList().stream().collect(Collectors.<LineStationEdge>toList()));
    }
}
