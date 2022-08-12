package nextstep.path.domain;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.line.domain.Sections;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {
    private final SimpleDirectedWeightedGraph<Long, SectionEdge> graph =
            new SimpleDirectedWeightedGraph<>(SectionEdge.class);

    public SubwayMap(List<Line> lines, PathSearchType type) {
        addVertexes(lines);
        addEdges(lines, type);
    }

    private void addVertexes(List<Line> lines) {
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);
    }

    private void addEdges(List<Line> lines, PathSearchType type) {
        List<Section> sections = lines.stream()
                .flatMap(it -> it.getSections().stream())
                .collect(Collectors.toList());

        List<Section> oppositeSections = lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(Section::flip)
                .collect(Collectors.toList());

        sections.addAll(oppositeSections);

        sections.forEach(it -> {
            SectionEdge edge = type.mapToEdge(it);
            graph.addEdge(edge.source(), edge.target(), edge);
            graph.setEdgeWeight(edge, edge.weight());
        });
    }

    public Path findPath(Long source, Long target) {
        DijkstraShortestPath<Long, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Long, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }
}