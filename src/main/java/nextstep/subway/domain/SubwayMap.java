package nextstep.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {
    private List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPathByDuration(Station source, Station target) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = getWeightedGraphByDuration();

        List<Section> sections = getResultSections(source, target, graph);

        return new Path(new Sections(sections));
    }

    public Path findPathByDistance(Station source, Station target) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = getWeightedGraphByDistance();

        List<Section> sections = getResultSections(source, target, graph);

        return new Path(new Sections(sections));
    }


    private SimpleDirectedWeightedGraph<Station, SectionEdge> getWeightedGraphByDuration() {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        addVertex(graph);

        lines.stream()
            .flatMap(it -> it.getSections().stream())
            .forEach(it -> setEdgeAndWeight(it, graph, it.getDuration()));

        lines.stream()
            .flatMap(it -> it.getSections().stream())
            .map(this::getSection)
            .forEach(it -> setEdgeAndWeight(it, graph, it.getDuration()));
        return graph;
    }

    private SimpleDirectedWeightedGraph<Station, SectionEdge> getWeightedGraphByDistance() {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        addVertex(graph);

        lines.stream()
            .flatMap(it -> it.getSections().stream())
            .forEach(it -> setEdgeAndWeight(it, graph, it.getDistance()));

        lines.stream()
            .flatMap(it -> it.getSections().stream())
            .map(this::getSection)
            .forEach(it -> setEdgeAndWeight(it, graph, it.getDistance()));
        return graph;
    }

    private Section getSection(Section section) {
        return new Section(section.getLine(), section.getDownStation(), section.getUpStation(), section.getDistance(), section.getDuration());
    }

    private void addVertex(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
            .flatMap(it -> it.getStations().stream())
            .distinct()
            .collect(Collectors.toList())
            .forEach(graph::addVertex);
    }

    private void setEdgeAndWeight(Section section, SimpleDirectedWeightedGraph<Station, SectionEdge> graph, int weight) {
        SectionEdge sectionEdge = SectionEdge.of(section);
        graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        graph.setEdgeWeight(sectionEdge, weight);
    }

    private List<Section> getResultSections(Station source, Station target, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        return result.getEdgeList().stream()
            .map(SectionEdge::getSection)
            .collect(Collectors.toList());
    }
}
