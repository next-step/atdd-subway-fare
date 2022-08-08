package nextstep.subway.domain;

import java.util.List;

public class Path {
    private static final int BASE_FARE = 1_250;
    private static final int BASE_DISTANCE = 10;

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

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int extractFare() {
        return BASE_FARE + calculateOverFare(extractDistance());
    }

    private int calculateOverFare(int distance) {
        if (distance <= BASE_DISTANCE) {
            return 0;
        }
        return (int) ((Math.ceil((distance - BASE_DISTANCE) / 5) + 1) * 100);
    }
}
