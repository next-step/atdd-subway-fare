package nextstep.subway.domain;

import nextstep.member.domain.Member;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {
    private List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPathAndFare(Station source, Station target, PathType type) {
        Sections shortestDistancePath = findShortestPath(source, target, PathType.DISTANCE);
        Fare fare = Fare.calculate(shortestDistancePath);
        if (type == PathType.DISTANCE) {
            return new Path(shortestDistancePath, fare);
        }
        Sections shortestDurationPath = findShortestPath(source, target, type);
        return new Path(shortestDurationPath, fare);
    }

    public Path findPathAndFare(Station source, Station target, PathType type, Member member) {
        Sections shortestDistancePath = findShortestPath(source, target, PathType.DISTANCE);
        Fare fare = Fare.calculate(shortestDistancePath, member);
        if (type == PathType.DISTANCE) {
            return new Path(shortestDistancePath, fare);
        }
        Sections shortestDurationPath = findShortestPath(source, target, type);
        return new Path(shortestDurationPath, fare);
    }

    private Sections findShortestPath(Station source, Station target, PathType type) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);
        // 지하철 역(정점)을 등록
        addVertex(graph);

        // 지하철 역의 연결 정보(간선)을 등록
        addEdge(graph, type);

        // 지하철 역의 연결 정보(간선)을 등록
        addReversedEdge(graph, type);

        // 다익스트라 최단 경로 찾기
        return getShortestPathSections(source, target, graph);
    }

    private void addVertex(SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);
    }

    private void addEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, PathType type) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, type.findWeight(it));
                });
    }

    private void addReversedEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, PathType type) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, type.findWeight(it));
                });

    }

    private Sections getShortestPathSections(Station source, Station target, SimpleDirectedWeightedGraph<Station, SectionEdge> graph) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());

        return new Sections(sections);
    }
}
