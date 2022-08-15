package nextstep.subway.domain;

public class FareCalculator {

    private static final int MINIMUM_FARE = 1250;
    private static final int FARE_PER_UNIT_DISTANCE = 100;
    private static final float FIRST_UNIT_DISTANCE = 5f;
    private static final float SECOND_UNIT_DISTANCE = 8f;

    private final int distance;

    public FareCalculator(int distance) {
        this.distance = distance;
    }

    public int getFare() {
        var firstRange = Math.min(50, distance) - 10;
        var secondRange = distance - 50;

        return MINIMUM_FARE
                + getFirstRangeOverFare(firstRange)
                + getSecondRangeOverFare(secondRange);
    }

    private int getFirstRangeOverFare(int distance) {
        return Math.max(0, (int) Math.ceil(distance / FIRST_UNIT_DISTANCE) * FARE_PER_UNIT_DISTANCE);
    }

    private int getSecondRangeOverFare(int distance) {
        return Math.max(0, (int) Math.ceil(distance / SECOND_UNIT_DISTANCE) * FARE_PER_UNIT_DISTANCE);
    }
}
