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

    @Deprecated
    public int extractFare() {
        return Fare.calculateAmount(extractDistance()) + getExtraFareOnLine();
    }

    public int getExtraFareOnLine() {
        return sections.getMaxExtraFareOnLine();
    }
}
