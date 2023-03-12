package nextstep.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {

    private final List<Line> lines;
    private final SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<Station, SectionEdge>(SectionEdge.class);
    private final FindType findType;

    public SubwayMap(final List<Line> lines, final FindType findType) {
        this.lines = lines;
        this.findType = findType;
    }

    public Path findPath(Station source, Station target) {
        registerVertex(lines);
        registerEdge(lines);

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
                .map(it -> it.getSection())
                .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }

    private void registerVertex(final List<Line> lines) {
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(it -> graph.addVertex(it));
    }

    private void registerEdge(final List<Line> lines) {
        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    setEdgeWeight(sectionEdge, it);
                });

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    setEdgeWeight(sectionEdge, it);
                });
    }

    private void setEdgeWeight(final SectionEdge sectionEdge, final Section section) {
        if (findType.equals(FindType.DISTANCE)) {
            graph.setEdgeWeight(sectionEdge, section.getDistance());
        }
        if (findType.equals(FindType.DURATION)) {
            graph.setEdgeWeight(sectionEdge, section.getDuration());
        }
    }
}
