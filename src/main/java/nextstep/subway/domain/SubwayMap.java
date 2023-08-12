package nextstep.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {
    private List<Line> lines;
    private PathType type;

    public SubwayMap(List<Line> lines, PathType type) {
        this.lines = lines;
        this.type = type;
    }

    public Path findPath(Station source, Station target) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);
        // 지하철 역(정점)을 등록
        addVertex(graph);

        // 지하철 역의 연결 정보(간선)을 등록
        addEdge(graph);

        // 지하철 역의 연결 정보(간선)을 등록
        addReversedEdge(graph);

        // 다익스트라 최단 경로 찾기
        return getShortestPath(source, target, graph);
    }

    private void addVertex(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);
    }

    private void addEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, type.findWeight(it));
                });
    }

    private void addReversedEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, type.findWeight(it));
                });

    }

    private Path getShortestPath(Station source, Station target, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }
}
