package atdd.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathGraph extends Graph {
    private List<Line> lines;

    public PathGraph(List<Line> lines) {
        super(lines);

        this.lines = lines;
    }

    public List<Station> getShortestDistancePath(Long startId, Long endId) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = makeGraph();
        graph = makePathGraph(graph);

        return getPathStations(graph, startId, endId);
    }

    private WeightedMultigraph<Long, DefaultWeightedEdge> makePathGraph(WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getEdges().stream())
                .forEach(it -> graph.setEdgeWeight(graph.addEdge(it.getSourceStation().getId(), it.getTargetStation().getId()), it.getDistance()));
        return graph;
    }
}
