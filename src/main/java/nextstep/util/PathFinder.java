package nextstep.util;

import nextstep.domain.subway.*;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;



public class PathFinder {
    private List<Line> lines;
    private PathType type;

    public PathFinder(List<Line> lines,PathType type) {
        this.lines = lines;
        this.type = type;

    }

    public Path findPath(Station source, Station target) {
        validateGraph(source, target);
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        // 지하철 역(정점)을 등록
        lines.stream()
                .flatMap(it -> it.getOrderedStationList().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    addEdge(graph, it);
                });

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
                .forEach(it -> {
                    addEdge(graph, it);
                });

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        if(Objects.equals(result,null)){
            throw new IllegalArgumentException("출발역과 도착역이 연결되어 있지 않음.");
        }


        List<Section> sections = result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());

        return new Path(new Sections(sections));
    }

    private void addEdge(SimpleDirectedWeightedGraph<Station, SectionEdge> graph, Section it) {
        SectionEdge sectionEdge = SectionEdge.of(it);
        graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
        graph.setEdgeWeight( sectionEdge, type.getWeight(it));
    }


    private void validateGraph(Station source, Station target) {

        if(source.getId()==target.getId()){
            throw new IllegalArgumentException("경로조회는 출발역과 도착역이 동일할 수 없음.");
        }

    }
}