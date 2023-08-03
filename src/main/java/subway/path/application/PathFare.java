package subway.path.application;

import org.jgrapht.graph.WeightedMultigraph;
import subway.line.domain.Section;
import subway.path.domain.SectionEdge;
import subway.station.domain.Station;

import java.util.List;

public class PathFare {
    private static final long BASIC_FARE = 1250L;
    private static final long FIRST_OVER_CHARGE_SECTION_BY_DISTANCE = 10L;
    private static final long FIRST_DIVISOR = 5L;
    private static final long SECOND_OVER_CHARGE_SECTION_BY_DISTANCE = 50L;
    private static final long SECOND_DIVISOR = 8L;
    private static final long REMAIN_SECTION_DISTANCE = SECOND_OVER_CHARGE_SECTION_BY_DISTANCE - FIRST_OVER_CHARGE_SECTION_BY_DISTANCE;
    private final GraphBuilder graph;

    public PathFare() {
        this.graph = new GraphBuilder(new ShortestDistancePathFinder());
    }

    public long calculateFare(final List<Section> sections, Station sourceStation, Station targetStation) {
        WeightedMultigraph<Station, SectionEdge> sectionGraph = graph.getGraph(sections);
        List<Section> sectionsInPath = graph.getPath(sectionGraph, sourceStation, targetStation);

        long totalFare = BASIC_FARE;
        Long distance = getTotalDistanceInPath(sectionsInPath);

        totalFare += calculateAdditionalFare(distance);

        return totalFare;
    }

    private Long getTotalDistanceInPath(List<Section> sections) {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(0L, Long::sum);
    }

    private long calculateAdditionalFare(Long distance) {
        long additionalFare = 0L;

        if (distance > SECOND_OVER_CHARGE_SECTION_BY_DISTANCE) {
            additionalFare += calculateOverFare(REMAIN_SECTION_DISTANCE, FIRST_DIVISOR);
            additionalFare += calculateOverFare(distance - SECOND_OVER_CHARGE_SECTION_BY_DISTANCE, SECOND_DIVISOR);
        } else if (distance > FIRST_OVER_CHARGE_SECTION_BY_DISTANCE) {
            additionalFare += calculateOverFare(distance - FIRST_OVER_CHARGE_SECTION_BY_DISTANCE, FIRST_DIVISOR);
        }

        return additionalFare;
    }

    private long calculateOverFare(long distance, long divisor) {
        return (long) ((Math.ceil((distance - 1) / divisor) + 1) * 100);
    }
}
