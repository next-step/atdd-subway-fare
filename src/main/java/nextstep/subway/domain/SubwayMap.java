package nextstep.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {
    private final List<Line> lines;
    private final SearchType searchType;
    private final SimpleDirectedWeightedGraph<Station, SectionEdge> graph;

    public SubwayMap(List<Line> lines, SearchType searchType) {
        this.lines = lines;
        this.searchType = searchType;
        this.graph = createGraph();
    }

    /**
     * 다익스트라 알고리즘을 이용한 최소 가중치의 경로 찾기
     */
    public Path findPath(Station source, Station target) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }

    private SimpleDirectedWeightedGraph<Station, SectionEdge> createGraph() {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        addVertex(graph);
        addEdge(graph);
        addEdgeForOppositePath(graph);

        return graph;
    }

    /**
     * 지하철 역(정점)을 등록
     */
    private void addVertex(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);
    }

    /**
     * 지하철 역의 연결 정보(간선)을 등록
     */
    private void addEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, getWeight(it));
                });
    }

    /**
     * 지하철 역의 연결 정보(간선)을 등록
     */
    private void addEdgeForOppositePath(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), getWeight(it), it.getDuration()))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, getWeight(it));
                });
    }

    private int getWeight(Section section) {
        if (searchType.isDistance()) {
            return section.getDistance();
        }
        if (searchType.isDuration()) {
            return section.getDistance();
        }

        throw new RuntimeException();
    }
}
