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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {


    public SubwayPath findPath(List<Line> lines, Long source, Long target, PathType type) {
        SubwayGraph graph = createSubwayGraph(lines, type);

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

    private SubwayPath convertSubwayPath(GraphPath graphPath) {
        return new SubwayPath((List<LineStationEdge>) graphPath.getEdgeList().stream().collect(Collectors.toList()));
    }
}
