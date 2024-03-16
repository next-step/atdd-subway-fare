package nextstep.subway.domain.pathfinder;

import nextstep.exception.PathSourceTargetNotConnectedException;
import nextstep.exception.PathSourceTargetSameException;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private WeightedMultigraph<Station, PathWeightedEdge> graph = new WeightedMultigraph<>(PathWeightedEdge.class);

    public PathFinder(List<Line> lineList, PathType pathType) {
        lineList.stream()
                .flatMap(line -> line.getSections().getSections().stream())
                .distinct()
                .forEach(section -> {
                    graph.addVertex(section.getUpStation());
                    graph.addVertex(section.getDownStation());
                    PathWeightedEdge pathWeightedEdge = graph.addEdge(section.getUpStation(), section.getDownStation());
                    if (PathType.DURATION == pathType) {
                        pathWeightedEdge.addDistance(section.getDistance());
                        graph.setEdgeWeight(pathWeightedEdge, section.getDuration());
                    } else {
                        pathWeightedEdge.addDuration(section.getDuration());
                        graph.setEdgeWeight(pathWeightedEdge, section.getDistance());
                    }

                });
    }

    public Path findPath(Station source, Station target, PathType pathType) {
        validateSourceAndTargetAreDifferent(source, target);

        DijkstraShortestPath<Station, PathWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, PathWeightedEdge> graphPath = shortestPath.getPath(source, target);

        validatePathExists(graphPath);

        Long distance;
        Long duration;

        if (PathType.DURATION == pathType) {
            distance = graphPath.getEdgeList().stream()
                    .mapToLong(edge -> edge.getDistance())
                    .sum();
            duration = (long) shortestPath.getPathWeight(source, target);
        } else {
            duration = graphPath.getEdgeList().stream()
                    .mapToLong(edge -> edge.getDuration())
                    .sum();
            distance = (long) shortestPath.getPathWeight(source, target);
        }

        return Path.builder()
                .path(graphPath.getVertexList())
                .distance(distance)
                .duration(duration)
                .build();
    }
    private void validateSourceAndTargetAreDifferent(Station source, Station target) {
        if (source.equals(target)) {
            throw new PathSourceTargetSameException("출발역과 도착역이 같으면 경로를 조회할 수 없습니다.");
        }
    }

    private void validatePathExists(GraphPath<Station, PathWeightedEdge> path) {
        if (path == null) {
            throw new PathSourceTargetNotConnectedException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }
}
