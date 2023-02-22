package nextstep.subway.domain;

import java.util.List;

import nextstep.subway.util.FareCalculator;

public class Path {

    private final Sections sections;

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
        return FareCalculator.calculate(extractDistance());
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
