package nextstep.subway.domain;

import nextstep.subway.desginpattern.DirectWeightGraphTemplate;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {
    private final List<Line> lines;

    public SubwayMap(final List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public Path findPathByShortestCondition(Station source, Station target, DirectWeightGraphTemplate directWeightGraphTemplate) {
        final SimpleDirectedWeightedGraph<Station, SectionEdge> graph = directWeightGraphTemplate.createGraph(lines);
        final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        final GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);
        final List<Section> sections = result.getEdgeList().stream()
                .map(it -> it.getSection())
                .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }
}
