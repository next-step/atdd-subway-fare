package nextstep.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubwayMap {
    private final List<Line> lines;
    private final PathType pathType;

    public SubwayMap(List<Line> lines, PathType pathType) {
        this.lines = lines;
        this.pathType = pathType;
    }

    public Path findPath(Station source, Station target) {
        WeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        addVertex(graph);
        addEdge(graph);

        GraphPath<Station, SectionEdge> result = findShortedPath(source, target, graph);

        List<Section> sections = result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }

    private GraphPath<Station, SectionEdge> findShortedPath(Station source, Station target, WeightedGraph<Station, SectionEdge> graph) {
        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(source, target);
    }

    private void addEdge(WeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역의 연결 정보(간선)을 등록 (역방향 포함)
        List<Section> sections =
                Stream.concat(
                        flatMap(lines, line -> line.getSections().stream()),
                        flatMap(lines, line -> line.getOppositeSections().stream())
                ).collect(Collectors.toList());

        sections.forEach(section -> addEdge(graph, section));
    }

    private void addEdge(WeightedGraph<Station, SectionEdge> graph, Section it) {
        SectionEdge sectionEdge = SectionEdge.of(it);
        graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
        graph.setEdgeWeight(sectionEdge, pathType.weight(it));
    }

    private void addVertex(WeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역(정점)을 등록
        List<Station> stations = flatMap(lines, line -> line.getStations().stream())
                .collect(Collectors.toList());
        stations.forEach(graph::addVertex);
    }

    private <T, R> Stream<R> flatMap(List<T> list, Function<T, Stream<R>> function) {
        return list.stream()
                .flatMap(function);
    }
}
