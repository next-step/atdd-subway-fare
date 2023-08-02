package subway.path.application;

import subway.line.domain.Section;
import subway.station.domain.Station;

import java.util.List;

public abstract class AbstractPathFinder {
    private static final long BASIC_FARE = 1250L;

    protected Long getTotalDurationInPath(List<Section> sections) {
        return sections.stream()
                .map(Section::getDuration)
                .reduce(0L, Long::sum);
    }

    protected Long getTotalDistanceInPath(List<Section> sections) {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(0L, Long::sum);
    }

    protected long calculateFare(final long distance) {
        long totalFare = BASIC_FARE;
        if (distance > 10 && distance <= 50) {
            totalFare += calculateOverFareWithTenDistance(distance - 10);
        }
        if (distance > 50) {
            totalFare += calculateOverFareWithTenDistance(40);
            totalFare += calculateOverFareWithFiftyDistance(distance - 50);
        }
        return totalFare;
    }
    private long calculateOverFareWithTenDistance(long distance) {
        return (long) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private long calculateOverFareWithFiftyDistance(long distance) {
        return (long) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
