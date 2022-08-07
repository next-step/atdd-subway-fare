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

    public Path findPath(Station source, Station target) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        // 지하철 역(정점)을 등록
        registerStationVertex(graph);

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDistance());
                });

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDistance());
                });

        // 다익스트라 최단 경로 찾기
        final List<Section> sections = getSectionRoute(source, target, graph);

        return new Path(new Sections(sections));
    }

    public Path findTimePath(final Station source, final Station target) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        // 지하철 역(정점)을 등록
        registerStationVertex(graph);

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
            .flatMap(it -> it.getSections().stream())
            .forEach(it -> {
                SectionEdge sectionEdge = SectionEdge.of(it);
                graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                graph.setEdgeWeight(sectionEdge, it.getDuration());
            });

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
            .flatMap(it -> it.getSections().stream())
            .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
            .forEach(it -> {
                SectionEdge sectionEdge = SectionEdge.of(it);
                graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                graph.setEdgeWeight(sectionEdge, it.getDuration());
            });

        // 다익스트라 최단 시간의 경로 찾기
        List<Section> sections = getSectionRoute(source, target, graph);

        return new Path(new Sections(sections));
    }

    private void registerStationVertex(final SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
            .flatMap(it -> it.getStations().stream())
            .distinct()
            .collect(Collectors.toList())
            .forEach(graph::addVertex);
    }

    private List<Section> getSectionRoute(final Station source, final Station target, final SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        return result.getEdgeList().stream()
            .map(SectionEdge::getSection)
            .collect(Collectors.toList());
    }

}
