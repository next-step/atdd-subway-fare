package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;
    private Fare fare;

    public Path(Sections sections) {
        this.sections = sections;
        this.fare = new Fare();
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

    public int getFare() {
        return fare.getFareByDistance(extractDistance());
    }
}
