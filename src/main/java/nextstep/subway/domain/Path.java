package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;

    private int fare;

    public Path(Sections sections, int fare) {
        this.sections = sections;
        this.fare = fare;
    }

    public Sections getSections() {
        return sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() { return sections.totalDuration(); }

    public int getFare() { return fare; }
    public List<Station> getStations() {
        return sections.getStations();
    }
}
