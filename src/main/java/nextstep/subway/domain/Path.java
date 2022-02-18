package nextstep.subway.domain;

import java.util.List;

public class Path {
    private final int DEFAULT_FARE = 1250;
    private final int DEFAULT_OVER_CHARGE_DISTANCE = 5;
    private final int EXTRA_CHARGE_START_DISTANCE = 50;
    private final int EXTRA_CHARGE = 100;
    private final int EXTRA_CHARGE_DISTANCE = 8;
    private final int AGE_CHILD_MIN = 6;
    private final long FARE_RATIO_CHILD = (long) 0.5;
    private final int AGE_YOUTH_MIN = 13;
    private final long FARE_RATIO_YOUTH = (long) 0.8;
    private final int AGE_ADULT_MIN = 19;
    private final int DEDUCTION_FARE = 350;
    private Sections sections;
    private int fare;

    protected Path() {
    }

    public Path(Sections sections, int fareDistance) {
        this.sections = sections;
        this.fare = DEFAULT_FARE + overFare(fareDistance) + maxLineFare();
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

    private int maxLineFare() {
        return sections.getSections().stream()
            .mapToInt(value -> value.getLine().getFare())
            .max().getAsInt();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getFare() {
        return fare;
    }

    protected int overFare(int distance) {
        if (distance <= EXTRA_CHARGE_START_DISTANCE) {
            return caculateDefaultOverFare(distance);
        }

        int overFare = caculateDefaultOverFare(EXTRA_CHARGE_START_DISTANCE);

        return overFare + caculateExtraFare(distance - EXTRA_CHARGE_START_DISTANCE);
    }

    private int caculateDefaultOverFare(int distance) {
        return (int) ((Math.ceil(((distance) - 1) / DEFAULT_OVER_CHARGE_DISTANCE) + 1) * 100) - 200;
    }

    private int caculateExtraFare(int extraDistance) {
        return ((extraDistance-1) / EXTRA_CHARGE_DISTANCE + 1) * EXTRA_CHARGE;
    }

    public void changeFareByAge(int age) {
        if (age < AGE_CHILD_MIN) {
            fare = 0;
            return;
        }

        if (age < AGE_YOUTH_MIN) {
            fare = (fare - DEDUCTION_FARE) / 10 * 5;
            return;
        }

        if (age < AGE_ADULT_MIN) {
            fare = (fare - DEDUCTION_FARE) / 10 * 8;
            return;
        }
    }

}
