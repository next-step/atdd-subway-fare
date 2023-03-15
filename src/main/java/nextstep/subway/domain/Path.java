package nextstep.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;
    private FarePolicy farePolicy;

    private int addFare;
    private int minDistance;

    public Path(final Sections sections,
                final FarePolicy farePolicy,
                final int addFare) {
        this(sections, farePolicy, addFare ,0);
    }

    public Path(final Sections sections,
                final FarePolicy farePolicy,
                final int addFare,
                final int minDistance) {
        this.sections = sections;
        this.farePolicy = farePolicy;
        this.addFare = addFare;
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
            return farePolicy.calculator(minDistance) + addFare;
        }
        return farePolicy.calculator(sections.totalDistance()) + addFare;
    }
}
