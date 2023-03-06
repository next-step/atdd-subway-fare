package nextstep.subway.domain;

import java.util.List;

public class Path {
    private final Sections sections;

    private final Fare fare;

    public Path(Sections sections) {
        this.sections = sections;
        this.fare = new Fare(sections.totalDistance());
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

    public int fare() {
        return fare.getFare();
    }
}
