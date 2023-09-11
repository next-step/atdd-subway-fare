package nextstep.subway.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import nextstep.subway.exception.impl.CannotFindPath;

public class SubwayMap {
    private List<Line> lines;
    private PathType type; // 경로 조회 타입

    public SubwayMap(List<Line> lines, PathType type) {
        this.lines = lines;
        this.type = type;
    }

    public Path findPath(Station source, Station target) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = generateGraph();

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
            .orElseThrow(CannotFindPath::new);

        List<Section> sections = result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }

    private SimpleDirectedWeightedGraph<Station, SectionEdge> generateGraph() {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        // 지하철 역(정점)을 등록
        lines.stream()
            .flatMap(it -> it.getStations().stream())
            .distinct()
            .collect(Collectors.toList())
            .forEach(it -> graph.addVertex(it));

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
            .flatMap(it -> it.getSections().get().stream())
            .forEach(it -> {
                SectionEdge sectionEdge = SectionEdge.of(it);
                graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                graph.setEdgeWeight(sectionEdge, getPathTypeEdgeWeight(it));
            });

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
            .flatMap(it -> it.getSections().get().stream())
            .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
            .forEach(it -> {
                SectionEdge sectionEdge = SectionEdge.of(it);
                graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                graph.setEdgeWeight(sectionEdge, getPathTypeEdgeWeight(it));
            });

        return graph;
    }

    private double getPathTypeEdgeWeight(Section section) {
        return PathType.DISTANCE.equals(this.type) ? section.getDistance() : section.getDuration();
    }
}
