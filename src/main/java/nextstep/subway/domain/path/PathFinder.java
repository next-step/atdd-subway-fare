package nextstep.subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;

public interface PathFinder {
    Path findPath(Station source, Station target);

    default Path getPath(Station source, Station target, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
            .map(SectionEdge::getSection)
            .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }

    default void addVertex(List<Line> lines, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역(정점)을 등록
        lines.stream()
            .flatMap(it -> it.getStations().stream())
            .distinct()
            .collect(Collectors.toList())
            .forEach(graph::addVertex);
    }
}
