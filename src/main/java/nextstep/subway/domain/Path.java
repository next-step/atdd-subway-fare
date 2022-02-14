package nextstep.subway.domain;

import java.util.List;

import nextstep.subway.domain.farepolicy.FarePolicy;

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

    public Object extractTotalCost(List<FarePolicy> farePolicies) {
        return null;
    }
}
