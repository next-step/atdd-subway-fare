package nextstep.subway.domain;

import nextstep.subway.domain.policy.FarePolicies;

import java.util.List;

public class Path {
    private Sections sections;
    private FarePolicies farePolicies;

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

    public void apply(FarePolicies farePolicies){
        this.farePolicies = farePolicies;
    }

    public int calculateFare(){
        return this.farePolicies.calculate(this.extractDistance());
    }
}
