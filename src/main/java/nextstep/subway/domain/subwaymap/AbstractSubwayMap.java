package nextstep.subway.domain.subwaymap;

import nextstep.subway.domain.*;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSubwayMap implements SubwayMap {

    final List<Line> lines;

    public AbstractSubwayMap(final List<Line> lines) {
        this.lines = lines;
    }

    @Override
    public Path findPath(Station source, Station target) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        addVertex(graph);
        addEdge(graph);

        return findShortestPath(graph, source, target);
    }

    private void addVertex(final SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);
    }

    abstract void addEdge(final SimpleDirectedWeightedGraph<Station, SectionEdge> graph);


    private Path findShortestPath(final SimpleDirectedWeightedGraph<Station, SectionEdge> graph, final Station source, final Station target) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }

}
