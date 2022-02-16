package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;

    private static final int DEFAULT_FARE = 1250;

    private int fareDistance;

    protected Path() {
    }

    public Path(Sections sections, int fareDistance) {
        this.sections = sections;
        this.fareDistance = fareDistance;
    }

    public static Path of(Sections sections, int fareDistance) {
        return new Path(sections, fareDistance);
    }

    public Sections getSections() {
        return sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    private int additionalFare() {
        if (hasAdditionalFareLine()) {
            return maxAdditionalFareLine();
        }
        return 0;
    }

    private boolean hasAdditionalFareLine() {
        return false;
    }

    private int maxAdditionalFareLine() {
        return 0;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getFare() {
        return DEFAULT_FARE + calculateOverFare(fareDistance) + additionalFare();
    }

    protected int calculateOverFare(int distance) {
        return (int) ((Math.ceil(((distance) - 1) / 5) + 1) * 100) - 200;
    }
}
