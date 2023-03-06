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

        return AgeFarePolicy.of(age)
                .calculateFare(fare);
    }
}
