package nextstep.subway.domain.map;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class SubwayMapGraphFactory {
    private SubwayMapGraphFactory() {
    }

    public static SimpleDirectedWeightedGraph<Station, SectionEdge> newOneFieldWeightGraph(
        List<Line> lines, Function<SectionEdge, Double> getWeightStrategy
    ) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = newEmptyGraph();
        addVertex(lines, graph);
        addEdgeWeight(graph, lines, getWeightStrategy);
        return graph;
    }

    private static SimpleDirectedWeightedGraph<Station, SectionEdge> newEmptyGraph() {
        return new SimpleDirectedWeightedGraph<>(SectionEdge.class);
    }

    private static void addVertex(List<Line> lines, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
             .flatMap(it -> it.getStations().stream())
             .distinct()
             .collect(Collectors.toList())
             .forEach(graph::addVertex);
    }

    private static void addEdgeWeight(
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph,
        List<Line> lines,
        Function<SectionEdge, Double> getFieldStrategy
    ) {
        lines.stream()
             .flatMap(it -> it.getSections().stream())
             .forEach(it -> {
                 SectionEdge sectionEdge = SectionEdge.of(it);
                 graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                 graph.setEdgeWeight(sectionEdge, getFieldStrategy.apply(sectionEdge));
             });
    }
}
