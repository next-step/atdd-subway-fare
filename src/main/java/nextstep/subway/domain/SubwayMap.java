package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.PathType;
import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubwayMap {
    private List<Line> lines;

    private DiscountPolicy discountPolicy;

    public SubwayMap(List<Line> lines) {
        this(lines, null);
    }

    public SubwayMap(List<Line> lines, DiscountPolicy discountPolicy) {
        this.lines = lines;
        this.discountPolicy = discountPolicy;
    }

    public Path findPath(final Station source,
                         final Station target,
                         final PathType type) {

        final GraphPath<Station, SectionEdge> result = getShortiesPath(source, target, type);

        final List<Section> sections = result.getEdgeList().stream()
                .map(SectionEdge::getSection)
                .collect(Collectors.toList());

        final int addFare = getMaxAddFare(sections);

        if (!type.equals(PathType.DISTANCE)) {
            final int minDistance = (int) getShortiesPath(source, target, PathType.DISTANCE).getWeight();
            return new Path(new Sections(sections), new DistanceFarePolicy(), discountPolicy, addFare, minDistance);
        }

        return new Path(new Sections(sections), new DistanceFarePolicy(), discountPolicy, addFare);
    }

    private int getMaxAddFare(final List<Section> sections) {
        return sections.stream()
                .mapToInt(station -> station.getLine().getAddFare())
                .max()
                .orElseThrow(NoSuchElementException::new);
    }

    private GraphPath<Station, SectionEdge> getShortiesPath(final Station source,
                                                            final Station target,
                                                            final PathType type) {
        SimpleDirectedWeightedGraph<Station, SectionEdge> graph = new SimpleDirectedWeightedGraph<>(SectionEdge.class);

        // 지하철 역(정점)을 등록
        addVertex(graph);
        // 지하철 역의 연결 정보(간선)을 등록
        addEdge(type, graph);

        return findShortiesPath(source, target, graph);
    }

    private GraphPath<Station, SectionEdge> findShortiesPath(final Station source,
                                                            final Station target,
                                                            final WeightedGraph<Station, SectionEdge> graph) {
        final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(source, target);
    }

    private void addEdge(final PathType type,
                         final WeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .flatMap(section -> Stream.of(section, createRevertSection(section)))
                .forEach(section -> setWeight(type, graph, section));
    }

    private Section createRevertSection(final Section section) {
        return new Section(section.getLine(), section.getDownStation(), section.getUpStation(), section.getDistance(), section.getDuration());
    }

    private void setWeight(final PathType type,
                           final WeightedGraph<Station, SectionEdge> graph,
                           final Section section) {
        final SectionEdge sectionEdge = SectionEdge.of(section);
        graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        graph.setEdgeWeight(sectionEdge, type.getTypeValue(section));
    }

    private void addVertex(final WeightedGraph<Station, SectionEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(graph::addVertex);
    }
}
