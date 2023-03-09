package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;
    private FarePolicy farePolicy;

    private int minDistance;

    public Path(final Sections sections,
                final FarePolicy farePolicy) {
        this(sections, farePolicy, 0);
    }

    public Path(final Sections sections,
                final FarePolicy farePolicy,
                final int minDistance) {
        this.sections = sections;
        this.farePolicy = farePolicy;
        this.minDistance = minDistance;
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
        if (minDistance > 0) {
            return farePolicy.calculator(minDistance);
        }
        return farePolicy.calculator(sections.totalDistance());
    }
}
