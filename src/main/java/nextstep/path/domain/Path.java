package nextstep.path.domain;

import nextstep.line.domain.Sections;

import java.util.List;

public class Path {
    private static final FareCalculator FARE_CALCULATOR = new FareCalculator();

    private final Sections sections;
    private final int shortestDistance;

    public static Path emptyPath() {
        return new Path(Sections.emptySections(), 0);
    }

    public Path(Sections sections, int shortestDistance) {
        this.sections = sections;
        this.shortestDistance = shortestDistance;
    }

    public List<Long> getStations() {
        return sections.getStations();
    }

    public int getDistance() {
        return sections.totalDistance();
    }

    public int getDuration() {
        return sections.totalDuration();
    }

    public int getFare() {
        return FARE_CALCULATOR.calculateFare(shortestDistance);
    }
}
