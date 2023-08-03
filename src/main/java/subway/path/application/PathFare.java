package subway.path.application;

import org.jgrapht.graph.WeightedMultigraph;
import subway.line.domain.Section;
import subway.path.domain.SectionEdge;
import subway.station.domain.Station;

import java.util.List;

public class PathFare {
    private final GraphBuilder graph;
    private static final long BASIC_FARE = 1250L;

    public PathFare() {
        this.graph = new GraphBuilder(new ShortestDistancePathFinder());
    }
    public long calculateFare(final List<Section> sections, Station sourceStation, Station targetStation) {
        WeightedMultigraph<Station, SectionEdge> graph = this.graph.getGraph(sections);
        List<Section> sectionsInPath = this.graph.getPath(graph, sourceStation, targetStation);

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
