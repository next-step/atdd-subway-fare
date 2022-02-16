package nextstep.subway.domain.map;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.WeightedMultigraph;
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
        return newPath(
            dijkstraShortestPath.getPath(source, target)
        );
    }

    public List<Path> findPaths(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, Station source, Station target) {
        KShortestPaths<Station, SectionEdge> kShortestPaths = new KShortestPaths<>(graph, 1000);
        return kShortestPaths.getPaths(source, target).stream()
                             .map(this::newPath)
                             .collect(Collectors.toList());
    }

    private Path newPath(GraphPath<Station, SectionEdge> graphPath) {
        List<Section> sections = graphPath.getEdgeList().stream()
                                          .map(SectionEdge::getSection)
                                          .collect(Collectors.toList());
        return new Path(new Sections(sections));
    }
}
