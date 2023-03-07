package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;
    private FarePolicy farePolicy;

    public Path(final Sections sections,
                final FarePolicy farePolicy) {
        this.sections = sections;
        this.farePolicy = farePolicy;
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

    public int getFare() {
        return farePolicy.calculator(sections.totalDistance());
    }
}
