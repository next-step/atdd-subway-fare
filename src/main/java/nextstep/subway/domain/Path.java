package nextstep.subway.domain;

import java.util.List;

public class Path {

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

    public int extractFare() {
        int distance = extractDistance();

        if (distance <= 10) {
            return 1250;
        }

        if (distance <= 50) {
            return 1250 + calculateOverFare(distance - 10, 5);
        }

        return 1250 + calculateOverFare(distance - 10, 8);
    }

    private int calculateOverFare(int distance, int kilometer) {
        return (int) ((Math.ceil((distance - 1) / kilometer) + 1) * 100);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
