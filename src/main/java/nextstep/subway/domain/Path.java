package nextstep.subway.domain;

import nextstep.subway.util.FarePolicy;
import nextstep.subway.util.NormalFarePolicy;

import java.util.List;

public class Path {

    private Sections sections;
    private FarePolicy farePolicy;

    public Path(Sections sections) {
        this.sections = sections;
        this.farePolicy = new NormalFarePolicy();
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
        return farePolicy.calculateFare(extractDistance());
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
