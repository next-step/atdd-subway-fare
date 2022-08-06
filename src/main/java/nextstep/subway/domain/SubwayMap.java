package nextstep.subway.domain;

import nextstep.subway.domain.graph.EdgeInitiator;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {
    private List<Line> lines;
    private SimpleDirectedWeightedGraph<Station, SectionEdge> graph;

    public SubwayMap(List<Line> lines) {
        if(lines.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.lines = lines;
        this.graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);
        initVertexes(graph);
    }

    public Path findPath(Station source, Station target, EdgeInitiator edgeInitiator) {
        edgeInitiator.initEdges(lines, graph);
        edgeInitiator.initOppositeEdges(lines, graph);
        List<Section> sections = findShortestPath(source, target);
        return new Path(new Sections(sections));
    }

    private List<Section> findShortestPath(Station source, Station target) {
        GraphPath<Station, SectionEdge> result = new DijkstraShortestPath<>(graph).getPath(source, target);

        return result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());
    }

    private void initVertexes(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(it -> graph.addVertex(it));
    }
}
