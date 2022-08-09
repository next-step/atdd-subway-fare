package nextstep.subway.domain;

import nextstep.subway.exception.NotConnectSectionException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubwayMap {
    private List<Line> lines;

    public SubwayMap(List<Line> lines) {
        this.lines = lines;
    }

    public Path findPath(Station source, Station target, PathType type, AgeDiscountPolicy ageDiscountPolicy) {
        // 다익스트라 최단 경로 찾기
        GraphPath<Station, SectionEdge> result = getGraphPath(source, target, type);

        validateConnectSection(result);

        Sections sections = new Sections(streamSections(result).collect(Collectors.toList()));

        int maxAdditionalFare = streamSections(result)
                .mapToInt(section -> section.getLine().getAdditionalFare())
                .max()
                .orElse(0);

        return new Path(sections, new Fare(sections.totalDistance(), maxAdditionalFare, ageDiscountPolicy));
    }

    private Stream<Section> streamSections(GraphPath<Station, SectionEdge> result) {
        return result.getEdgeList().stream()
                .map(SectionEdge::getSection);
    }

    private void validateConnectSection(GraphPath<Station, SectionEdge> result) {
        if (result == null) {
            throw new NotConnectSectionException();
        }
    }

    private GraphPath<Station, SectionEdge> getGraphPath(Station source, Station target, PathType type) {
        DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(getGraph(type));
        return dijkstraShortestPath.getPath(source, target);
    }

    private SimpleDirectedWeightedGraph<Station, SectionEdge> getGraph(PathType type) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        // 지하철 역(정점)을 등록
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(it -> graph.addVertex(it));

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, type.getEdgeWeight(it));
                });

        // 지하철 역의 연결 정보(간선)을 등록
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .map(it -> new Section(
                                it.getLine(),
                                it.getDownStation(),
                                it.getUpStation(),
                                it.getDistance(),
                                it.getDuration()
                        )
                )
                .forEach(it -> {
                    SectionEdge sectionEdge = SectionEdge.of(it);
                    graph.addEdge(it.getUpStation(), it.getDownStation(), sectionEdge);
                    graph.setEdgeWeight(sectionEdge, type.getEdgeWeight(it));
                });
        return graph;
    }
}
