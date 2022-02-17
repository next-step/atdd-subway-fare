package nextstep.subway.domain.map.graphfactory;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Station;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public abstract class SubwayMapGraphFactory {
    public SimpleDirectedWeightedGraph<Station, SectionEdge> createGraph(List<Line> lines) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);
        addVertex(lines, graph);
        addEdge(lines, graph);
        return graph;
    }

    private void addVertex(List<Line> lines, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역(정점)을 등록
        lines.stream()
             .flatMap(it -> it.getStations().stream())
             .distinct()
             .collect(Collectors.toList())
             .forEach(graph::addVertex);
    }

    protected abstract void addEdge(List<Line> lines, SimpleDirectedWeightedGraph<Station, SectionEdge> graph);
}
