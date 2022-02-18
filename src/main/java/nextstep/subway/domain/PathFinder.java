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
        GraphPath<Station, DefaultWeightedEdge> path;
        try {
            path = type.getPath(dijkstraShortestPath, dijkstraShortestPathDuration, source, target);
        } catch (IllegalArgumentException e) {
            throw new PathException("노선에 등록되지 않은 역입니다.");
        }
        if (path == null) {
            throw new PathException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
        return path.getVertexList();
    }

    public Path shortsPath(Station source, Station target, PathType type) {
        int sum = 0;
        List<Station> stations = shortsPathStations(source, target, type);
        for (Line line : lines) {
            List<Section> sections = line.getSections();
            for (Section section : sections) {
                for (int i = 0; i < stations.size() - 1; i++) {
                    if (section.getUpStation().equals(stations.get(i))) {
                        if (section.getDownStation().equals(stations.get(i + 1))) {
                            if (type == PathType.DISTANCE) {
                                sum += section.getDuration();
                            }
                            if (type == PathType.DURATION) {
                                sum += section.getDistance();
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (type == PathType.DISTANCE) {
            return new Path(type.getPathWeight(dijkstraShortestPath, source, target), sum);
        }
        return new Path(sum, type.getPathWeight(dijkstraShortestPathDuration, source, target));
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
