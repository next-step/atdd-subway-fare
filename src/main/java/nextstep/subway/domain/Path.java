package nextstep.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.List;
import java.util.Objects;

public class Path {
    private final List<Long> vertexs;
    private final List<SectionWeightedEdge> edges;
    private final double weight;

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
        this.vertexs = graphPath.getVertexList();
        this.edges = graphPath.getEdgeList();
        this.weight = dijkstraShortestPath.getPathWeight(source, target);
    }

    public List<Long> getVertexs() {
        return vertexs;
    }

    public double getWeight() {
        return weight;
    }

    public int getDistance() {
        return edges.stream().mapToInt(SectionWeightedEdge::getDistance).sum();
    }

    public int getDuration() {
        return edges.stream().mapToInt(SectionWeightedEdge::getDuration).sum();
    }
}
