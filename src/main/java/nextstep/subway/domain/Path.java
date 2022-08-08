package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;
    private int overFare;

    public Path(Sections sections, int overFare) {
        this.sections = sections;
        this.overFare = overFare;
    }

    public Path(Sections sections) {
        this(sections, 0);
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
        return overFare + FarePolicy.calculateFare(sections.totalDistance());
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

}
