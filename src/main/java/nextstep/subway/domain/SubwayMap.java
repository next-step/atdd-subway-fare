package nextstep.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayMap {
    private List<Line> lines;
    private SimpleDirectedWeightedGraph<Station, SectionEdge> graph;
    private FareCalcurator fareCalcurator = new FareCalcurator();

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPath(Station source, Station target, PathType type) {
        Sections sections = findShortestPathSections(source, target);

        int fare = fareCalcurator.calculate(sections.totalDistance());

        if(type.equals(PathType.DURATION)) {
            sections = findFastPathSections(source, target);
        }

        return new Path(sections, fare);
    }

    private Sections findShortestPathSections(Station source, Station target) {
        graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        setVertex();

        setEdgeAndWeight();

        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
                .map(it -> it.getSection())
                .collect(Collectors.toList());

        return new Sections(sections);
    }

    private Sections findFastPathSections(Station source, Station target) {
        graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        setVertex();

        setEdgeAndWeight();


        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SectionEdge> result = dijkstraShortestPath.getPath(source, target);

        List<Section> sections = result.getEdgeList().stream()
                .map(it -> it.getSection())
                .collect(Collectors.toList());

        return new Sections(sections);
    }

    private void setVertex(){
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(it -> graph.addVertex(it));

    }

    private void setEdgeAndWeight(){
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDuration());
                });

        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(it.getLine(), it.getDownStation(), it.getUpStation(), it.getDistance(), it.getDuration()))
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, it.getDistance());
                });
    }
}
