package nextstep.subway.desginpattern;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Station;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;

public abstract class DirectWeightGraphTemplate {

    public SimpleDirectedWeightedGraph<Station, SectionEdge> createGraph(List<Line> lines) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        addVertex(lines, graph);
        addEdge(lines, graph);
        addOppositeEdge(lines, graph);

        return graph;
    }

    protected abstract void addVertex(List<Line> lines, SimpleDirectedWeightedGraph<Station, SectionEdge> graph);

    protected abstract void addEdge(List<Line> lines, SimpleDirectedWeightedGraph<Station, SectionEdge> graph);

    protected abstract void addOppositeEdge(List<Line> lines, SimpleDirectedWeightedGraph<Station, SectionEdge> graph);
}
