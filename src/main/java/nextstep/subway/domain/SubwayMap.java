package nextstep.subway.domain;

import static nextstep.subway.domain.FindPathType.DISTANCE;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class SubwayMap {

    private List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public static Path findPath(String typeName, Station upStation, Station downStation, List<Line> lines) {
        return findPath(typeName, upStation, downStation, lines, 100);
    }

    public static Path findPath(String typeName, Station upStation, Station downStation, List<Line> lines, int age) {
        SubwayMap subwayMap = new SubwayMap(lines);
        FindPathType findPathType = FindPathType.find(typeName);
        return subwayMap.findPath(upStation, downStation, findPathType, age);
    }

    public static void findPath(Station upStation, Station downStation, List<Line> lines) {
        findPath(DISTANCE.name(), upStation, downStation, lines, 100);
    }

    private Path findPath(Station source, Station target, FindPathType findPathType, int age) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        // 지하철 역(정점)을 등록
        registerStation(graph);

        // 지하철 역의 연결 정보(간선)을 등록
        connectStationsWithWeight(graph, findPathType);

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);
        if (result == null) {
            throw new NoSuchElementException("경로가 존재하지 않습니다.");
        }

        List<Section> sectionList = getSections(result);
        Sections sections = new Sections(sectionList);
        int fare = FareCalculator.getFare(sections.totalDistance(),lines,sectionList,age);
        return new Path(sections, fare);
    }

    private void registerStation(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);
    }

    private void connectStationsWithWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph,
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
