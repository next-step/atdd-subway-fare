package nextstep.subway.path;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.edge.EdgeInitiator;
import nextstep.subway.exception.SubwayMapException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class SubwayMap {

    private final List<Line> lines;
    private final SimpleDirectedWeightedGraph<Station, SectionEdge> graph;

    public SubwayMap(List<Line> lines) {
        if (lines.isEmpty()) {
            throw new SubwayMapException("지하철 노선이 존재하지 않습니다.");
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
        GraphPath<Station, SectionEdge> result = new DijkstraShortestPath<>(graph).getPath(source,
            target);

        return result.getEdgeList().stream()
            .map(SectionEdge::getSection)
            .collect(Collectors.toList());
    }

    private void initVertexes(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
            .flatMap(it -> it.getStations().stream())
            .distinct()
            .collect(Collectors.toList())
            .forEach(graph::addVertex);
    }
}
