package nextstep.subway.domain;

import java.util.Optional;
import java.util.stream.Stream;
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

    public Path findPath(final Station source, final Station target, final PathType pathType) {
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

    private void addNode(final WeightedGraph graph, final PathType pathType) {
        addEdge(getSectionStream(), graph, pathType);
        addEdge(getSectionStream().map(SubwayMap::createReverseSectionBy), graph, pathType);
    }

    private Stream<Section> getSectionStream() {
        return lines.stream().flatMap(it -> it.getSections().stream());
    }

    private void addEdge(final Stream<Section> lineStream, final WeightedGraph graph, final PathType pathType) {
        lineStream.forEach(it -> setEdge(graph, pathType, it));
    }

    private static void setEdge(
            final WeightedGraph<Station, SectionEdge> graph,
            final PathType pathType,
            final Section it
    ) {
        SectionEdge sectionEdge = SectionEdge.of(it);
        graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
        graph.setEdgeWeight(sectionEdge, pathType.getStrategy().apply(it));
    }

    private static Section createReverseSectionBy(final Section section) {
        return new Section(
                section.getLine(),
                section.getDownStation(),
                section.getUpStation(),
                section.getDistance(),
                section.getDuration()
        );
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

            return new Path(new Sections(sections), new DistanceFarePolicy(new BasicDistanceFareFormula()));
        } catch (IllegalArgumentException exception) {
            throw new PathNotFoundException();
        }
    }
}
