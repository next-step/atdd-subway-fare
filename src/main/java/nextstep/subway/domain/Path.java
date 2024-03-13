package nextstep.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.List;
import java.util.Objects;

public class Path {
    private final List<Long> stations;
    private final List<SectionWeightedEdge> edges;

    public Path(DijkstraShortestPath dijkstraShortestPath, Long source, Long target) {
        GraphPath graphPath;
        try {
            graphPath = dijkstraShortestPath.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("경로에 존재하지 않는 역입니다.");
        }

        if(Objects.isNull(graphPath)) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }
        this.stations = graphPath.getVertexList();
        this.edges = graphPath.getEdgeList();
    }

    public Path(List<Long> vertexs, List<Section> sections, double weight) {
        this.stations = vertexs;
        this.edges = null;
        this.sections = sections;
    }

    public List<Long> getStations() {
        return stations;
    }

    public int getDistance() {
        return edges.stream().mapToInt(SectionWeightedEdge::getDistance).sum();
    }

    public int getDuration() {
        return edges.stream().mapToInt(SectionWeightedEdge::getDuration).sum();
    }
}
