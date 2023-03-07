package nextstep.subway.domain;

import nextstep.subway.domain.fare.Fare;

import java.util.List;

public class Path {
    private final Sections sections;

    private final Fare fare;

    public Path(Sections sections, Fare fare) {
        this.sections = sections;
        this.fare = fare;
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

    public int calculateFare() {
        return fare.calculateFare(this.sections.totalDistance());
    }
}
