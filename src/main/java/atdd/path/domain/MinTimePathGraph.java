package atdd.path.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class MinTimePathGraph extends Graph {
    private List<Line> lines;

    public MinTimePathGraph(List<Line> lines) {
        super(lines);

        this.lines = lines;
    }

    public List<Station> getShortestTimeDistancePath(Long startId, Long endId) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = makeGraph();
        graph = makeMinTimePathGraph(graph);

        return getPathStations(graph, startId, endId);
    }

    private WeightedMultigraph<Long, DefaultWeightedEdge> makeMinTimePathGraph(WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getEdges().stream())
                .forEach(it -> graph.setEdgeWeight(graph.addEdge(it.getSourceStation().getId(), it.getTargetStation().getId()), it.getElapsedTime()));
        return graph;
    }
}
