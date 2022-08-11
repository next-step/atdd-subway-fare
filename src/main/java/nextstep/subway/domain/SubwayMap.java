package nextstep.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {
    private List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPath(Station source, Station target, EdgeWeightStrategy weightStrategy) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        // 지하철 역(정점)을 등록
        addStationVertex(graph);

        // 지하철 역의 연결 정보(간선)을 등록
        addSectionEdge(graph, weightStrategy);

        // 지하철 역의 연결 정보(간선)을 등록
        addEdge(graph, weightStrategy);

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        return new Path(getSections(result));
    }

    private Sections getSections(GraphPath<Station, SectionEdge> result) {
        return new Sections(result.getEdgeList()
                .stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList()));
    }

    private void addEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, EdgeWeightStrategy weightStrategy) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, weightStrategy.getEdgeWeight(it));
                });
    }

    private void addSectionEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, EdgeWeightStrategy weightStrategy) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, weightStrategy.getEdgeWeight(it));
                });
    }

    private void addStationVertex(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .forEach(graph::addVertex);
    }

}
