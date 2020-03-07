package atdd.path.domain;

import atdd.path.application.dto.MinTimePathResponseView;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class Graph {
    private List<Line> lines;

    public Graph(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<Station> getShortestDistancePath(Long startId, Long endId) {
        return getPathStations(makeGraph(lines), startId, endId);
    }

    public WeightedMultigraph<Long, DefaultWeightedEdge> makeGraph(List<Line> lines) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .forEach(it -> graph.addVertex(it.getId()));

        lines.stream()
                .flatMap(it -> it.getEdges().stream())
                .forEach(it -> graph.setEdgeWeight(graph.addEdge(it.getSourceStation().getId(), it.getTargetStation().getId()), it.getDistance()));
        return graph;
    }

    private List<Station> getPathStations(WeightedMultigraph<Long, DefaultWeightedEdge> graph, Long startId, Long endId) {
        GraphPath<Long, DefaultWeightedEdge> path = new DijkstraShortestPath(graph).getPath(startId, endId);
        return path.getVertexList().stream()
                .map(it -> findStation(it))
                .collect(Collectors.toList());
    }

    public Station findStation(Long stationId) {
        return lines.stream()
                .flatMap(it -> it.getStations().stream())
                .filter(it -> it.getId() == stationId)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public MinTimePathResponseView getMinTimePath(Long stationId, Long stationId4) {
        return getMinTimePathStations(makeGraph(lines), stationId, stationId4);
    }

    private MinTimePathResponseView getMinTimePathStations(WeightedMultigraph<Long, DefaultWeightedEdge> graph, Long startId, Long endId) {
        List<GraphPath<Long, DefaultWeightedEdge>> paths
                = new KShortestPaths(graph, 1000).getPaths(startId, endId);
        GraphPath<Long, DefaultWeightedEdge> graphPath = paths.get(0);
        MinTimePath minTimePath = new MinTimePath(lines, graphPath);
        MinTimePathResponseView responseView
                = minTimePath.findPathStationsResponseView(graphPath, startId, endId);
        return responseView;
    }
}
