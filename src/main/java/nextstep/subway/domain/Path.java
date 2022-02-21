package nextstep.subway.domain;

import java.util.List;
import nextstep.subway.domain.farepolicy.DiscountFarePolicy;
import nextstep.subway.domain.farepolicy.FarePolicy;
import nextstep.subway.domain.farepolicy.MaxLineFarePolicy;
import nextstep.subway.domain.farepolicy.OverFarePolicy;

public class Path {
    private Sections sections;
    private int fareDistance;
    private FarePolicy farePolicy = new FarePolicy();

    protected Path() {
    }

    private Path(Sections sections, int fareDistance) {
        this.sections = sections;
        this.fareDistance = fareDistance;
    }

    public static Path of(Sections sections, int fareDistance) {
        return new Path(sections, fareDistance);
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

    private int calculateFare() {
        return farePolicy.calculateFare();
    }

    public int extractFare() {
        return calculateFare();
    }

    public void farePolicySetting(int age) {
        this.farePolicy
            .appendPolicy(new OverFarePolicy(fareDistance))
            .appendPolicy(new MaxLineFarePolicy(sections))
            .appendPolicy(new DiscountFarePolicy(age));
    }
}
