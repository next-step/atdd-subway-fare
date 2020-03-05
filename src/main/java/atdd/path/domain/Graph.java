package atdd.path.domain;

import atdd.path.application.dto.MinTimePathResponseView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.GraphWalk;
import org.jgrapht.graph.WeightedMultigraph;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private WeightedMultigraph<Long, DefaultWeightedEdge> makeGraph(List<Line> lines) {
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

    private Station findStation(Long stationId) {
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
        List<GraphPath<Long, DefaultWeightedEdge>> paths = new KShortestPaths(graph, 1000).getPaths(startId, endId);
        GraphPath<Long, DefaultWeightedEdge> graphPath = paths.get(0);


        double distance_km = graphPath.getWeight();
        int velocity_kmPerHour = 50;
        double time_hour = distance_km/velocity_kmPerHour;
        double time_min = 60 * time_hour;

        LocalDateTime departBy = LocalDateTime.now();
        LocalDateTime arriveBy = departBy.plusMinutes(Math.round(time_min));

        List<Station> stations = graphPath.getVertexList().stream()
                .map(it -> findStation(it))
                .collect(Collectors.toList());

        Set<Line> linesForPath = new HashSet<>();
        for(Station station:stations){
            lines.stream()
                    .filter(it -> it.getStations().contains(station))
                    .forEach(it -> linesForPath.add(it));
        }

        MinTimePathResponseView responseView
                = new MinTimePathResponseView(startId, endId, stations, linesForPath,
                distance_km, departBy, arriveBy);

        return responseView;
    }
}
