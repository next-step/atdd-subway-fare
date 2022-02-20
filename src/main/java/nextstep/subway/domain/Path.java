package nextstep.subway.domain;

import java.util.List;

public class Path {
    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_DISTANCE = 10;

    private Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
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

    public int extractFare() {
        if (sections.totalDistance() <= DEFAULT_DISTANCE) {
            return DEFAULT_FARE;
        }
        return DEFAULT_FARE + calculateOverFare(sections.totalDistance() - DEFAULT_DISTANCE);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
