package nextstep.subway.domain;

import java.math.BigDecimal;
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

    public int extractFare() {
        return Fare.calculateAmount(extractDistance());
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
