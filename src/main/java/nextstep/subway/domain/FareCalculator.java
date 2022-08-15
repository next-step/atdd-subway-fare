package nextstep.subway.domain;

public class FareCalculator {

    private static final int MINIMUM_FARE = 1250;
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
        return Math.max(0, (int) Math.ceil(distance / 5f) * 100);
    }

    private int getSecondRangeOverFare(int distance) {
        return Math.max(0, (int) Math.ceil(distance / 8f) * 100);
    }
}
