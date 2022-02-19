package nextstep.subway.domain;

import nextstep.subway.ui.exception.PathException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class PathFinder {
    private final Lines lines;

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;
    private final PathType type1;

    public PathFinder(List<Line> lines, PathType type1) {
        this.lines = new Lines(lines);
        this.type1 = type1;
        dijkstraShortestPath = createDijkstraShortestPath();
    }

    public List<Station> shortsPathStations(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = type1.getPath(dijkstraShortestPath, source, target);
        if (path == null) {
            throw new PathException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
        return path.getVertexList();
    }

    public Path shortsPath(Station source, Station target) {
        List<Station> stations = shortsPathStations(source, target);

        if (type1 == PathType.DISTANCE) {
            return new Path(type1.getPathWeight(dijkstraShortestPath, source, target), lines.pathTotalDuration(stations));
        }
        return new Path(lines.pathTotalDistance(stations), type1.getPathWeight(dijkstraShortestPath, source, target));
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> createDijkstraShortestPath() {
        Set<Station> stations = lines.getStations();
        List<Section> sections = lines.getSections();

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : stations) {
            graph.addVertex(station);
        }

        type1.setEdgeWeight(sections, graph);
        return new DijkstraShortestPath<>(graph);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathFinder that = (PathFinder) o;
        return Objects.equals(lines, that.lines) && Objects.equals(dijkstraShortestPath, that.dijkstraShortestPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines, dijkstraShortestPath);
    }
}
