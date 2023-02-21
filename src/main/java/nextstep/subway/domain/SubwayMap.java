package nextstep.subway.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class SubwayMap {

    private final List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPath(Station source, Station target, PathSearchType pathSearchType) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        // 지하철 역(정점)을 등록
        lines.stream()
            .flatMap(it -> it.getStations().stream())
            .distinct()
            .collect(Collectors.toList())
            .forEach(graph::addVertex);

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
            .flatMap(it -> it.getSections().stream())
            .forEach(it -> addSectionEdge(graph, it, pathSearchType));

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
            .flatMap(it -> it.getSections().stream())
            .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
            .forEach(it -> addSectionEdge(graph, it, pathSearchType));

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
            .map(SectionEdge::getSection)
            .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }

    private void addSectionEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, Section section, PathSearchType pathSearchType) {
        SectionEdge sectionEdge = SectionEdge.of(section);
        graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        graph.setEdgeWeight(sectionEdge, getWeight(section, pathSearchType));
    }

    private int getWeight(Section section, PathSearchType pathSearchType) {
        if (pathSearchType.isDistance()) {
            return section.getDistance();
        }
        return section.getDuration();
    }
}
