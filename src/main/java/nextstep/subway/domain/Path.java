package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;
    private List<FarePolicy> farePolicies;

    public Path(Sections sections, FarePolicy... farePolicy) {
        this.sections = sections;
        this.farePolicies = List.of(farePolicy);
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

    public int calculateFare() {
        return farePolicies.stream()
                .mapToInt(farePolicy -> farePolicy.apply(this))
                .sum();
    }
}
