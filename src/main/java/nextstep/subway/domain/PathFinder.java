package nextstep.subway.domain;

import nextstep.subway.ui.exception.PathException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class PathFinder {
    private final List<Line> lines;

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPathDuration;

    public PathFinder(List<Line> lines) {
        this.lines = lines;
        dijkstraShortestPath = createDijkstraShortestPath(PathType.DISTANCE);
        dijkstraShortestPathDuration = createDijkstraShortestPath(PathType.DURATION);
    }

    public List<Station> shortsPathStations(Station source, Station target, PathType type) {
        GraphPath<Station, DefaultWeightedEdge> path = type.getPath(dijkstraShortestPath,
                                                                    dijkstraShortestPathDuration,
                                                                    source, target);
        if (path == null) {
            throw new PathException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
        return path.getVertexList();
    }

    public int shortsPathDistance(Station source, Station target) {
        return (int)dijkstraShortestPath.getPathWeight(source, target);
    }

    public int shortsPathDistanceDurationTest(Station source, Station target) {
        return (int)dijkstraShortestPathDuration.getPathWeight(source, target);
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> createDijkstraShortestPath(PathType type) {
        Set<Station> stations = new HashSet<>();
        List<Section> sections = new ArrayList<>();
        for (Line line : lines) {
            stations.addAll(line.getStations());
            sections.addAll(line.getSections());
        }

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : stations) {
            graph.addVertex(station);
        }

        type.setEdgeWeight(sections, graph);
        return new DijkstraShortestPath<>(graph);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PathFinder that = (PathFinder) o;
        return Objects.equals(lines, that.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines);
    }
}
