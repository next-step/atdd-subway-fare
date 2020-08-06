package nextstep.subway.maps.map.application;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.map.domain.*;
import nextstep.subway.maps.map.dto.PathRequest;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public SubwayPath findPath(List<Line> lines, PathRequest pathRequest) {

        PathType type = pathRequest.getType();
        if (type == PathType.ARRIVAL_TIME) {
            return findPathByArrivalTime(lines, pathRequest);
        }

        SubwayGraph graph = new SubwayGraph(LineStationEdge.class);
        graph.addVertexWith(lines);
        graph.addEdge(lines, type);

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Long, LineStationEdge> path = dijkstraShortestPath.getPath(pathRequest.getSource(), pathRequest.getTarget());

        return convertSubwayPath(path);
    }

    private SubwayPath findPathByArrivalTime(List<Line> lines, PathRequest pathRequest) {
        List<SubwayPath> allPath = findAllPath(lines, pathRequest);
        SubwayPaths subwayPaths = new SubwayPaths(allPath);
        return subwayPaths.getEarliestAlightTimePath(pathRequest.getTime());
    }

    public List<SubwayPath> findAllPath(List<Line> lines, PathRequest pathRequest) {
        SubwayGraph graph = new SubwayGraph(LineStationEdge.class);
        graph.addVertexWith(lines);
        graph.addEdge(lines);
        List<GraphPath<Long, LineStationEdge>> paths = new KShortestPaths<>(graph, 1000).getPaths(pathRequest.getSource(), pathRequest.getTarget());
        return paths.stream().map(this::convertSubwayPath).collect(Collectors.toList());
    }

    @SuppressWarnings({"unchecked", "SimplifyStreamApiCallChains", "rawtypes"})
    private SubwayPath convertSubwayPath(GraphPath graphPath) {
        return new SubwayPath((List<LineStationEdge>) graphPath.getEdgeList().stream().collect(Collectors.<LineStationEdge>toList()));
    }
}
