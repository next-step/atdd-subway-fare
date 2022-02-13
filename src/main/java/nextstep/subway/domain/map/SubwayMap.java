package nextstep.subway.domain.map;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionEdge;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;

@Component
public class SubwayMap {
    public Path findPath(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, Station source, Station target) {
        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
                                       .map(SectionEdge::getSection)
                                       .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }
}
