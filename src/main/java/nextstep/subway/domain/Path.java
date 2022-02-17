package nextstep.subway.domain;

import java.util.List;

public class Path {
    private final int DEFAULT_FARE = 1250;
    private final int DEFAULT_OVER_CHARGE_DISTANCE = 5;
    private final int EXTRA_CHARGE_START_DISTANCE = 50;
    private final int EXTRA_CHARGE = 100;
    private final int EXTRA_CHARGE_DISTANCE = 8;

    private Sections sections;

    private int fareDistance;

    protected Path() {
    }

    public Path(Sections sections, int fareDistance) {
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

    public int getFare() {
        return DEFAULT_FARE + calculateOverFare(fareDistance);
    }

    protected int calculateOverFare(int distance) {
        if (distance <= EXTRA_CHARGE_START_DISTANCE) {
            return (int) ((Math.ceil(((distance) - 1)
                / DEFAULT_OVER_CHARGE_DISTANCE) + 1) * 100) - 200;
        }

        int overFare = (int) ((Math.ceil(((EXTRA_CHARGE_START_DISTANCE) - 1)
            / DEFAULT_OVER_CHARGE_DISTANCE) + 1) * 100) - 200;

        return overFare + caculateExtraFare(distance - EXTRA_CHARGE_START_DISTANCE);
    }

    private int caculateExtraFare(int extraDistance) {
        return ((extraDistance-1) / EXTRA_CHARGE_DISTANCE + 1) * EXTRA_CHARGE;
    }
}
