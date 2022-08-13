package nextstep.subway.domain;

import nextstep.subway.domain.strategy.WeightStrategy;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SubwayMap {
    private List<Line> lines;
    private List<Section> foundPathResult;
    private PathType type;

    public SubwayMap(List<Line> lines, PathType type) {
        this.lines = lines;
        this.type = type;
    }

    public Path findPath(Station source, Station target) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        initializeStation(graph);
        initializeSectionEdge(graph, type.weightStrategy());

        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        foundPathResult = extractSections(result);
        return new Path(foundPathResult);
    }

    public Fare findFare(Station source, Station target) {
        if (type.isDistanceType() && hasFoundPathResult()) {
            return new Fare(foundPathResult);
        }

        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        initializeStation(graph);
        initializeSectionEdge(graph, PathType.DISTANCE.weightStrategy());

        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        foundPathResult = extractSections(result);
        return new Fare(foundPathResult);
    }

    private boolean hasFoundPathResult() {
        return !Objects.isNull(foundPathResult);
    }

    private void initializeStation(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);
    }

    private void initializeSectionEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, WeightStrategy weightStrategy) {
        registerSection(graph, weightStrategy);
        registerReverseSection(graph, weightStrategy);
    }

    private void registerSection(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, WeightStrategy weightStrategy) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, weightStrategy.weight(it));
                });
    }

    private void registerReverseSection(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, WeightStrategy weightStrategy) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(Section::reverseOf)
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, weightStrategy.weight(it));
                });
    }

    private List<Section> extractSections(GraphPath<Station, SectionEdge> result) {
        return result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toUnmodifiableList());
    }
}
