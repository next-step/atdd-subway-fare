package nextstep.subway.domain.map;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.springframework.stereotype.Component;

@Component
public class SubwayMap {
    public Path findPath(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, Station source, Station target) {
        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return newPath(
            dijkstraShortestPath.getPath(source, target)
        );
    }

    public Paths findPaths(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, Station source, Station target) {
        KShortestPaths<Station, SectionEdge> kShortestPaths = new KShortestPaths<>(graph, 1000);
        return kShortestPaths.getPaths(source, target).stream()
                             .map(this::newPath)
                             .collect(Collectors.collectingAndThen(
                                 Collectors.toList(), Paths::new
                             ));
    }

    private Path newPath(GraphPath<Station, SectionEdge> graphPath) {
        List<Section> sections = graphPath.getEdgeList().stream()
                                          .map(SectionEdge::getSection)
                                          .collect(Collectors.toList());
        return new Path(new Sections(sections));
    }
}
