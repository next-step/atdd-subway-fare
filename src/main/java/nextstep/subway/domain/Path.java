package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public Fare extractFare() {
        return Fare.chaining().calculate(sections.totalDistance());
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
