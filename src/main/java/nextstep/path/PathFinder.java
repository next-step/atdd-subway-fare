package nextstep.path;

import nextstep.exception.SubwayException;
import nextstep.line.Line;
import nextstep.section.Section;
import nextstep.station.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;

public class PathFinder {

    private final List<Line> lines;

    public PathFinder(List<Line> lines) {
        this.lines = lines;
    }

    public PathResponse findPath(Station source, Station target, PathType pathType) {
        validateEqualsStation(source, target);

        WeightedMultigraph<Station, PathWeightEdge> graph = createGraph(pathType);
        validateStationExists(graph, source, target);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, PathWeightEdge> path = Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
                .orElseThrow(() -> new SubwayException("출발역과 도착역이 연결이 되어 있지 않습니다."));

        int distance = path.getEdgeList().stream().mapToInt(PathWeightEdge::getDistance).sum();
        int duration = path.getEdgeList().stream().mapToInt(PathWeightEdge::getDuration).sum();
        return new PathResponse(path.getVertexList(), distance, duration);
    }

    private void validateEqualsStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new SubwayException("출발역과 도착역이 같습니다.");
        }
    }

    private WeightedMultigraph<Station, PathWeightEdge> createGraph(PathType pathType) {
        WeightedMultigraph<Station, PathWeightEdge> graph = new WeightedMultigraph(PathWeightEdge.class);

        lines.stream()
                .flatMap(line -> line.getSections().stream())
                .distinct()
                .forEach(section -> {
                    Station upStation = section.getUpStation();
                    Station downStation = section.getDownStation();
                    PathWeightEdge edge = new PathWeightEdge(section.getDistance(), section.getDuration());
                    graph.addVertex(downStation);
                    graph.addVertex(upStation);
                    graph.addEdge(upStation, downStation, edge);
                    graph.setEdgeWeight(edge, pathType.getWeight(section));
                });
        return graph;
    }

    private void validateStationExists(WeightedMultigraph<Station, PathWeightEdge> graph,
                                       Station source, Station target) {
        if (!graph.containsVertex(source) || !graph.containsVertex(target)) {
            throw new SubwayException("존재하지 않은 역입니다.");
        }
    }

    public void isValidateRoute(Station source, Station target, PathType type) {
        this.findPath(source, target, type);
    }
}
