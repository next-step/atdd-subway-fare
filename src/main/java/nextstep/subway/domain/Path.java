package nextstep.subway.domain;

import java.util.List;

public class Path {
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

    public int calculateFare(int age) {
        int fare = Fare.calculate(extractDistance()) + sections.findSurCharge();

        if (age < 13) {
            return (int) ((fare - 350) * 0.5);
        }

        if (age < 19) {
            return (int) ((fare - 350) * 0.8);
        }

        return fare;
    }
}
