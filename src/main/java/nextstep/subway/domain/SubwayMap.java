package nextstep.subway.domain;

import nextstep.subway.domain.sectiontype.SectionPathType;
import nextstep.subway.domain.sectiontype.SectionPathTypes;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {
    private List<Line> lines;
    private SectionPathType sectionPathType;
    private SectionPathTypes sectionPathTypes = new SectionPathTypes();

    public SubwayMap(List<Line> lines, SectionPathType sectionPathType) {
        this.lines = lines;
        this.sectionPathType = sectionPathType;
    }

    public Path findPath(Station source, Station target) {
        // 그래프 만들기
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = createGraph();

        // 다익스트라 최단/최소 시간 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
                .map(it -> it.getSection())
                .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }

    private SimpleDirectedWeightedGraph<Station, SectionEdge> createGraph() {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        addVertex(graph);
        addEdge(graph);
        addOppositeEdge(graph);

        return graph;
    }

    private void addVertex(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역(정점)을 등록
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(it -> graph.addVertex(it));
    }

    private void addEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역의 연결 정보(간선)을 등록

        for (Line line : lines) {
            for (Section section : line.getSections()) {
                SectionEdge sectionEdge = SectionEdge.of(section);
                graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);

                sectionPathTypes.setEdgeWeight(sectionPathType, graph, section, sectionEdge);
            }
        }
    }

    private void addOppositeEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역의 연결 정보(간선)을 등록

        for (Line line : lines) {
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



}
