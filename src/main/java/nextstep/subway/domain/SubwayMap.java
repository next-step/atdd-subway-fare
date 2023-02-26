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
        return toSectionsBy(graphPath).getResultPath();
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
        addEdge(graph, pathSearchType);

        return new DijkstraShortestPath<>(graph);
    }

    private void addVertex(WeightedGraph<Station, SectionEdge> graph) {
        this.lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);
    }

    private void addEdge(WeightedGraph<Station, SectionEdge> graph, PathSearchType pathSearchType) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> addSectionEdge(graph, it, pathSearchType));
    }


    private void addSectionEdge(
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

    private Sections toSectionsBy(GraphPath<Station, SectionEdge> graphPath) {
        return new Sections(
                graphPath.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList())
        );
    }
}
