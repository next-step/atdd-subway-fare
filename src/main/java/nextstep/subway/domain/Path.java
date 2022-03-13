package nextstep.subway.domain;

import java.util.List;

public class Path {
    private static final int DEFAULT_FARE = 1250;

    private final Sections sections;

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
        return DEFAULT_FARE + calcOverFare();
    }

    private int calcOverFare() {
        int distance = extractDistance();
        int overFare = 0;

        while (distance > 10) {
            if (distance > 50) {
                distance -= 8;
                overFare += 100;
                continue;
            }

            distance -= 5;
            overFare += 100;
        }
        return overFare;
    }
}
