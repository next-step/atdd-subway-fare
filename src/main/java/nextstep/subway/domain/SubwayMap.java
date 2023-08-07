package nextstep.subway.domain;

import static nextstep.subway.domain.FindPathType.DISTANCE;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class SubwayMap {

    private List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPath(Station source, Station target, FindPathType findPathType) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        // 지하철 역(정점)을 등록
        registerStation(graph);

        // 지하철 역의 연결 정보(간선)을 등록
        connectStationsWithDistance(graph, findPathType);

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = getSections(result);

        return new Path(new Sections(sections));
    }

    private void registerStation(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);
    }

    private void connectStationsWithDistance(SimpleDirectedWeightedGraph<Station, SectionEdge> graph,
            FindPathType findPathType) {
        connectStationsInOrder(graph, findPathType);
        connectStationsInReverseOrder(graph, findPathType);
    }

    private void connectStationsInReverseOrder(SimpleDirectedWeightedGraph<Station, SectionEdge> graph,
            FindPathType findPathType) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(),
                        it.getDuration()))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    int weight = getWeight(findPathType, it);
                    graph.setEdgeWeight(sectionEdge, weight);
                });
    }

    private void connectStationsInOrder(SimpleDirectedWeightedGraph<Station, SectionEdge> graph,
            FindPathType findPathType) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    int weight = getWeight(findPathType, it);
                    graph.setEdgeWeight(sectionEdge, weight);
                });
    }

    private static int getWeight(FindPathType findPathType, Section it) {
        return findPathType.equals(DISTANCE) ? it.getDistance() : it.getDuration();
    }

    private static List<Section> getSections(GraphPath<Station, SectionEdge> result) {
        return result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());
    }
}
