package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.PathResponse;
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
    private final PathType type;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(List<Line> lines, PathType type) {
        this.lines = new Lines(lines);
        this.type = type;
        dijkstraShortestPath = createDijkstraShortestPath();
    }

    public PathResponse shortsPath(Station source, Station target) {
        List<Station> stations = shortsPathStations(source, target);

        if (type == PathType.DISTANCE) {
            return new PathResponse(stations, (int)dijkstraShortestPath.getPathWeight(source, target), lines.pathTotalDuration(stations));
        }
        return new PathResponse(stations, lines.pathTotalDistance(stations), (int)dijkstraShortestPath.getPathWeight(source, target));
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> createDijkstraShortestPath() {
        Set<Station> stations = lines.getStations();
        List<Section> sections = lines.getSections();

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(stations, graph);
        setEdgeWeight(sections, graph);

        return new DijkstraShortestPath<>(graph);
    }

    private void addVertex(Set<Station> stations, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void setEdgeWeight(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), type.weight(section));
        }
    }

    private List<Station> shortsPathStations(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = getPath(source, target);
        if (path == null) {
            throw new PathException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
        return path.getVertexList();
    }

    private GraphPath<Station, DefaultWeightedEdge> getPath(Station source, Station target) {
        try {
            return dijkstraShortestPath.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new PathException("노선에 등록되지 않은 역입니다.");
        }
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
        return Objects.equals(lines, that.lines) && Objects.equals(dijkstraShortestPath, that.dijkstraShortestPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lines, dijkstraShortestPath);
    }
}
