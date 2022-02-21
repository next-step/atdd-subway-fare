package nextstep.subway.domain;

public class From10To50Policy implements SubwayFarePolicy {
    private static final int EXTRA_FARE = 100;
    private static final int EXTRA_STEP = 5;
    private static final int MIN_DISTANCE = 10;
    private static final int MAX_DISTANCE = 50;

    @Override
    public int apply(int currentFare, int distance) {
        if (distance <= MIN_DISTANCE) {
            return currentFare;
        }

        if (MIN_DISTANCE < distance && distance <= MAX_DISTANCE) {
            return currentFare + calculateOverFare(distance - MIN_DISTANCE);
        }

        if (MAX_DISTANCE < distance) {
            return currentFare + calculateOverFare(MAX_DISTANCE - MIN_DISTANCE);
        }

        throw new IllegalArgumentException();
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / EXTRA_STEP) + 1) * EXTRA_FARE);
    }
}
