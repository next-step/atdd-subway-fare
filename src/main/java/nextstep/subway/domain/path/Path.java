package nextstep.subway.domain.path;

import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.fare.BasicFarePolicy;
import nextstep.subway.domain.fare.FarePolicy;

import java.util.List;

public class Path {
    private Sections sections;
    private FarePolicy farePolicy;

    public Path(Sections sections) {
        this.sections = sections;
        this.farePolicy = new BasicFarePolicy();
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

    public long calculateFare() {
        return this.farePolicy.calculateOverFare(extractDistance());
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
