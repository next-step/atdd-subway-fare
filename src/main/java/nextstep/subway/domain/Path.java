package nextstep.subway.domain;

import nextstep.subway.domain.policy.FarePolicies;

import java.util.List;

public class Path {
    private final Sections sections;
    private final FarePolicies farePolicies = new FarePolicies();

    public Path(Sections sections) {
        this.sections = sections;
    }

    public Sections getSections() {
        return sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public int calculateFare(){
        return this.farePolicies.calculate(this.extractDistance());
    }
}
