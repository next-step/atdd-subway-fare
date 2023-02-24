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

    public int extractFare(int age) {
        int fare = Fare.calculate(extractDistance()) + sections.extraFare();
        return fare - extractDiscountedFare(fare, age);
    }

    private int extractDiscountedFare(int fare, int age) {
        if (age >= 20) {
            return 0;
        }
        if (age > 12) {
            return (int)((fare - 350) * 0.2);
        }
        return (int)((fare - 350) * 0.5);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
