package nextstep.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {

    private final List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public Path findPath(Station source, Station target, PathSearchType pathSearchType) {
        GraphPath<Station, SectionEdge> graphPath = makeGraph(source, target, pathSearchType);
        return new Path(new Sections(getResultBy(graphPath)));
    }

    private GraphPath<Station, SectionEdge> makeGraph(
            Station source,
            Station target,
            PathSearchType pathSearchType) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = build(pathSearchType);
        return dijkstraShortestPath.getPath(source, target);
    }

    private DijkstraShortestPath<Station, SectionEdge> build(PathSearchType pathSearchType) {
        WeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        addVertex(graph);
        addEdge(pathSearchType, graph);

        return new DijkstraShortestPath<>(graph);
    }

    private void addVertex(WeightedGraph<Station, SectionEdge> graph) {
        this.lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);
    }

    private void addEdge(PathSearchType pathSearchType, WeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> addEdge(graph, it, pathSearchType));
    }


    private void addEdge(
            WeightedGraph<Station, SectionEdge> graph,
            Section section,
            PathSearchType pathSearchType) {
        SectionEdge sectionEdge = SectionEdge.of(section);
        graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        graph.setEdgeWeight(sectionEdge, getWeight(section, pathSearchType));
    }

    private int getWeight(Section section, PathSearchType pathSearchType) {
        return pathSearchType.isDistance() ? section.getDistance() : section.getDuration();
    }

    private List<Section> getResultBy(GraphPath<Station, SectionEdge> graphPath) {
        return graphPath.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());
    }
}
