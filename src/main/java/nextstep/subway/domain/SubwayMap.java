package nextstep.subway.domain;

import java.util.Optional;
import nextstep.subway.domain.exception.PathNotFoundException;
import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {
    private final List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPath(Station source, Station target, PathType pathType) {
        WeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);
        addVertex(graph);
        addNode(graph, pathType);
        return findShortestPath(source, target, graph);
    }

    private void addVertex(final WeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);
    }

    private void addNode(final WeightedGraph<Station, SectionEdge> graph, final PathType pathType) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, pathType.getStrategy().apply(it));
                });

        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, pathType.getStrategy().apply(it));
                });
    }

    private static Path findShortestPath(
            final Station source,
            final Station target,
            final WeightedGraph<Station, SectionEdge> graph
    ) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        try {
            GraphPath<Station, SectionEdge> result = Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
                    .orElseThrow(PathNotFoundException::new);

            List<Section> sections = result.getEdgeList().stream()
                    .map(SectionEdge::getSection)
                    .collect(Collectors.toList());

            return new Path(new Sections(sections));
        } catch (IllegalArgumentException exception) {
            throw new PathNotFoundException();
        }
    }
}
