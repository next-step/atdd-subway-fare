package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;
    private Fare fare;

    public Path(Sections sections, Fare fare) {
        this.sections = sections;
        this.fare = fare;
    }

    public Path(Sections sections) {
        this(sections, new Fare(sections.totalDistance()));
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

    public int getFare() {
        return fare.value();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

}
