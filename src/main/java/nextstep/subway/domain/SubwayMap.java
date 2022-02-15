package nextstep.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {
    private List<Line> lines;

    private SimpleDirectedWeightedGraph<Station, SectionEdge> graph;
    private DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath;
    private GraphPath<Station, SectionEdge> result;
    private int fareDistance;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPath(Station source, Station target, PathType pathType) {
        // 그래프 만들기
        graph = createGraph(PathType.DISTANCE);

        // 다익스트라 최단 경로 찾기
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
            .map(SectionEdge::getSection)
            .collect(Collectors.toList());

        fareDistance = result.getEdgeList().stream()
            .mapToInt(value -> value.getSection().getDistance())
            .sum();

        if (pathType.equals(PathType.DURATION)) {
            graph = createGraph(PathType.DURATION);
            dijkstraShortestPath = new DijkstraShortestPath<>(graph);
            result = dijkstraShortestPath.getPath(source, target);

            sections = result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());
        }

        return Path.of(new Sections(sections), fareDistance);
    }

    private SimpleDirectedWeightedGraph<Station, SectionEdge> createGraph(PathType pathType) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph
            = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        addVertex(graph);
        addWeights(graph, pathType);

        return graph;
    }

    private void addWeights(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, PathType pathType) {
        if (pathType.equals(PathType.DISTANCE)) {
            addEdgeDistanceWeight(graph);
            addOppositeEdgeDistanceWeight(graph);
            return;
        }

        addEdgeDurationWeight(graph);
        addOppositeEdgeDurationWeight(graph);
    }


    private void addVertex(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역(정점)을 등록
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);
    }

    private void addEdgeDistanceWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDistance());
                });
    }

    private void addOppositeEdgeDistanceWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(
                    it.getLine(),
                    it.getDownStation(),
                    it.getUpStation(),
                    it.getDistance(),
                    it.getDuration()
                ))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDistance());
                });
    }

    private void addEdgeDurationWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDuration());
                });
    }

    private void addOppositeEdgeDurationWeight(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(
                    it.getLine(),
                    it.getDownStation(),
                    it.getUpStation(),
                    it.getDistance(),
                    it.getDuration()
                ))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDuration());
                });
    }
}
