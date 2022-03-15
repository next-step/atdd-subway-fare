package nextstep.subway.domain;

import nextstep.subway.domain.sectiontype.SectionPathType;
import nextstep.subway.domain.sectiontype.SectionPathTypes;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubwayMap {
    private SectionPathTypes sectionPathTypes;

    public SubwayMap(SectionPathTypes sectionPathTypes) {
        this.sectionPathTypes = sectionPathTypes;
    }

    public Path findPath(List<Line> lines, SectionPathType sectionPathType, Station source, Station target) {
        // 그래프 만들기
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = createGraph(lines, sectionPathType);

        // 다익스트라 최단/최소 시간 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
                .map(it -> it.getSection())
                .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }

    private SimpleDirectedWeightedGraph<Station, SectionEdge> createGraph(List<Line> lines, SectionPathType sectionPathType) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        addVertex(lines, graph);
        addEdge(lines, sectionPathType, graph);
        addOppositeEdge(lines, sectionPathType, graph);

        return graph;
    }

    private void addVertex(List<Line> lines, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역(정점)을 등록
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(it -> graph.addVertex(it));
    }

    private void addEdge(List<Line> lines, SectionPathType sectionPathType, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역의 연결 정보(간선)을 등록

        for (Line line : lines) {
            addEdgeForStation(line.getSections(), sectionPathType, graph);
        }
    }

    private void addEdgeForStation(List<Section> sections, SectionPathType sectionPathType, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        for (Section section : sections) {
            SectionEdge sectionEdge = SectionEdge.of(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);

            sectionPathTypes.setEdgeWeight(sectionPathType, graph, section, sectionEdge);
        }
    }

    private void addOppositeEdge(List<Line> lines, SectionPathType sectionPathType, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역의 연결 정보(간선)을 등록

        for (Line line : lines) {
            addOppositeEdgeForStation(graph, line, sectionPathType);
        }
    }

    private void addOppositeEdgeForStation(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, Line line, SectionPathType sectionPathType) {
        List<Section> sectionList = line.getSections().stream()
                .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
                .collect(Collectors.toList());

        for (Section section : sectionList) {
            SectionEdge sectionEdge = SectionEdge.of(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);

            sectionPathTypes.setEdgeWeight(sectionPathType, graph, section, sectionEdge);
        }
    }


}
