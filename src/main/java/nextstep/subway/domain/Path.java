package nextstep.subway.domain;

import org.jgrapht.GraphPath;

import java.util.List;

public class Path {
    private final List<Long> vertexs;
    private final List<SectionWeightedEdge> edges;

    public Path(GraphPath graphPath) {
        this.vertexs = graphPath.getVertexList();
        this.edges = graphPath.getEdgeList();
    }

    public int getDistance() {
        return edges.stream().mapToInt(SectionWeightedEdge::getDistance).sum();
    }

    public int getDuration() {
        return edges.stream().mapToInt(SectionWeightedEdge::getDuration).sum();
    }
}
