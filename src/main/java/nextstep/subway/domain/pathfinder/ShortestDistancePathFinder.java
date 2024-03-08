package nextstep.subway.domain.pathfinder;

import nextstep.exception.PathSourceTargetNotConnectedException;
import nextstep.exception.PathSourceTargetSameException;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;


public class ShortestDistancePathFinder {
    private WeightedMultigraph<Station, CustomWeightedEdge> graph = new WeightedMultigraph<>(CustomWeightedEdge.class);
    public ShortestDistancePathFinder(List<Line> lineList) {
        lineList.stream()
                .flatMap(line -> line.getSections().getSections().stream())
                .distinct()
                .forEach(section -> {
                    graph.addVertex(section.getUpStation());
                    graph.addVertex(section.getDownStation());
                    CustomWeightedEdge customWeightedEdge = graph.addEdge(section.getUpStation(), section.getDownStation());
                    customWeightedEdge.addDuration(section.getDuration());
                    graph.setEdgeWeight(customWeightedEdge, section.getDistance());
                });
    }

    public Path findPath(Station source, Station target) {
        validateSourceAndTargetAreDifferent(source, target);

        DijkstraShortestPath<Station, CustomWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, CustomWeightedEdge> path = shortestPath.getPath(source, target);

        validatePathExists(path);

        Long duration = path.getEdgeList().stream()
                .mapToLong(edge -> edge.getDuration())
                .sum();
        return Path.builder()
                .path(path.getVertexList())
                .duration(duration)
                .distance((long) shortestPath.getPathWeight(source, target))
                .build();
    }

    private void validateSourceAndTargetAreDifferent(Station source, Station target) {
        if (source.equals(target)) {
            throw new PathSourceTargetSameException("출발역과 도착역이 같으면 경로를 조회할 수 없습니다.");
        }
    }

    private void validatePathExists(GraphPath<Station, CustomWeightedEdge> path) {
        if (path == null) {
            throw new PathSourceTargetNotConnectedException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }
}
