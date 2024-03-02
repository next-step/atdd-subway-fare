package nextstep.subway.domain.strategy;

import nextstep.exception.ApplicationException;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Dijkstra implements ShortestPathStrategy {

    private final DijkstraShortestPath dijkstraShortestPath;

    public Dijkstra(List<Section> sections, PathType pathType) {
        WeightedMultigraph<Station, SectionProxy> graph = new WeightedMultigraph<>(SectionProxy.class);
        addVertexes(sections, graph);
        addEdges(sections, graph, pathType);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void addVertexes(List<Section> sections, WeightedMultigraph<Station, SectionProxy> graph) {
        for (Section section : sections) {
            graph.addVertex(section.upStation());
            graph.addVertex(section.downStation());
        }
    }

    private void addEdges(List<Section> sections, WeightedMultigraph<Station, SectionProxy> graph, PathType pathType) {
        for (Section section : sections) {
            SectionProxy sectionProxy = new SectionProxy(section, pathType.getType().apply(section));
            graph.addEdge(sectionProxy.getSourceVertex(), sectionProxy.getTargetVertex(), sectionProxy);
        }
    }

    @Override
    public List<Station> findShortestPath(Station source, Station target) {
        GraphPath shortestPath = getPath(source, target);
        validateExistPath(shortestPath);
        return shortestPath.getVertexList();
    }

    @Override
    public List<Section> findEdges(Station source, Station target) {
        GraphPath shortestPath = getPath(source, target);
        validateExistPath(shortestPath);
        List<SectionProxy> edges = shortestPath.getEdgeList();
        return edges.stream()
                .map(SectionProxy::toSection)
                .collect(Collectors.toList());
    }

    @Override
    public long findShortestValue(Station source, Station target) {
        GraphPath shortestPath = getPath(source, target);
        validateExistPath(shortestPath);
        return (long) shortestPath.getWeight();
    }

    private GraphPath getPath(Station source, Station target) {
        try {
            return dijkstraShortestPath.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException("노선에 존재하지 않는 지하철역입니다.");
        }
    }

    private void validateExistPath(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        if (Objects.isNull(shortestPath)) {
            throw new ApplicationException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }
}
