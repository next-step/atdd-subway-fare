package nextstep.path;

import lombok.RequiredArgsConstructor;
import nextstep.exception.PathNotFoundException;
import nextstep.line.LineRepository;
import nextstep.line.section.Section;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PathFinder {
    private final LineRepository lineRepository;

    private WeightedMultigraph<String, CustomWeightedEdge> init(Function<Section, Integer> weightExtractor) {
        WeightedMultigraph<String, CustomWeightedEdge> graph = new WeightedMultigraph<>(CustomWeightedEdge.class);
        lineRepository.findAll().forEach(line -> {
            line.getSections().forEach(section -> {
                String upStationName = Long.toString(section.getUpstation().getId());
                String downStationName = Long.toString(section.getDownstation().getId());

                graph.addVertex(upStationName);
                graph.addVertex(downStationName);

                CustomWeightedEdge edge = graph.addEdge(upStationName, downStationName);
                if (edge != null) {
                    int weight = weightExtractor.apply(section);
                    edge.setDistance(section.getDistance());
                    edge.setDuration(section.getDuration());
                    graph.setEdgeWeight(edge, weight);
                }
            });
        });

        return graph;
    }

    public PathInfo findFastestPath(String sourceId, String targetId) {
        return findPath(sourceId, targetId, Section::getDuration);
    }

    public PathInfo findShortestPath(String sourceId, String targetId) {
        return findPath(sourceId, targetId, Section::getDistance);
    }

    private PathInfo findPath(String sourceId, String targetId, Function<Section, Integer> weightExtractor) {
        WeightedMultigraph<String, CustomWeightedEdge> graph = init(weightExtractor);
        DijkstraShortestPath<String, CustomWeightedEdge> dijkstra = new DijkstraShortestPath<>(graph);
        GraphPath<String, CustomWeightedEdge> path = dijkstra.getPath(sourceId, targetId);

        if (path == null) {
            throw new PathNotFoundException("가능한 경로가 존재하지 않습니다.");
        }

        List<String> pathVertices = path.getVertexList();
        int totalDistance = path.getEdgeList().stream()
                .mapToInt(CustomWeightedEdge::getDistance)
                .sum();
        int totalDuration = path.getEdgeList().stream()
                .mapToInt(CustomWeightedEdge::getDuration)
                .sum();

        return new PathInfo(pathVertices, totalDistance, totalDuration);
    }

    public boolean pathExists(String sourceId, String targetId) {
        WeightedMultigraph<String, CustomWeightedEdge> graph = init(section -> section.getDistance());
        DijkstraShortestPath<String, CustomWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<String, CustomWeightedEdge> path = dijkstraShortestPath.getPath(sourceId, targetId);
        return path != null;
    }
}
