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

    public int calculateFare() {
        return Fare.calculate(extractDistance(), sections.calculateLineExtraFare());
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
