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

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public int calculateFare(int age) {
        int fare = sections.calculateFare();

        if (age >= 13 && age < 19) {
            return (int) ((fare - 350) * 0.8);
        } else if (age >= 6 && age < 13) {
            return (int) ((fare - 350) * 0.5);
        }

        return fare;
    }
}
