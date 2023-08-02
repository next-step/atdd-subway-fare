package subway.path.application;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.constant.SubwayMessage;
import subway.exception.SubwayBadRequestException;
import subway.line.domain.Section;
import subway.path.domain.SectionEdge;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathFare {
    private final GraphBuilder graphBuilder;
    private static final long BASIC_FARE = 1250L;

    public PathFare() {
        this.graphBuilder = new GraphBuilder(new ShortestDistancePathFinder());
    }
    public long calculateFare(final List<Section> sections, Station sourceStation, Station targetStation) {
        WeightedMultigraph<Station, SectionEdge> graph = graphBuilder.getGraph(sections);
        List<Section> sectionsInPath = graphBuilder.getPath(graph, sourceStation, targetStation);

        long totalFare = BASIC_FARE;
        Long distance = getTotalDistanceInPath(sectionsInPath);

        totalFare += calculateAdditionalFare(distance);

        return totalFare;
    }

    private long calculateAdditionalFare(Long distance) {
        long additionalFare = 0L;

        if (distance > 50) {
            additionalFare += calculateOverFareWithTenDistance(40);
            additionalFare += calculateOverFareWithFiftyDistance(distance - 50);
        } else if (distance > 10) {
            additionalFare += calculateOverFareWithTenDistance(distance - 10);
        }

        return additionalFare;
    }

    private WeightedMultigraph<Station, SectionEdge> getGraphByCalculateFare(List<Section> sections) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        List<Station> stations = getStations(sections);

        stations.forEach(graph::addVertex);
        sections.forEach(section -> {
            SectionEdge sectionEdge = new SectionEdge(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
            graph.setEdgeWeight(sectionEdge, section.getDistance());
        });

        return graph;
    }

    private List<Section> getPath(WeightedMultigraph<Station, SectionEdge> graph,
                                  Station sourceStation,
                                  Station targetStation) {
        try {
            final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
            final List<SectionEdge> edgeList = dijkstraShortestPath.getPath(sourceStation, targetStation).getEdgeList();
            return edgeList.stream()
                    .map(SectionEdge::getSection)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SubwayBadRequestException(SubwayMessage.PATH_NOT_CONNECTED_IN_SECTION);
        }
    }

    private List<Station> getStations(List<Section> sections) {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }

    private Long getTotalDistanceInPath(List<Section> sections) {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(0L, Long::sum);
    }


    private long calculateOverFareWithTenDistance(long distance) {
        return (long) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private long calculateOverFareWithFiftyDistance(long distance) {
        return (long) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
