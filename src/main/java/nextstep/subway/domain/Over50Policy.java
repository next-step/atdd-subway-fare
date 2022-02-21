package nextstep.subway.domain;

public class Over50Policy implements SubwayFarePolicy {
    private static final int EXTRA_FARE = 100;
    private static final int EXTRA_STEP = 8;
    private static final int MIN_DISTANCE = 50;

    @Override
    public int apply(int currentFare, int distance) {
        if (distance <= MIN_DISTANCE) {
            return currentFare;
        }

        if (MIN_DISTANCE < distance) {
            return currentFare + calculateOverFare(distance - MIN_DISTANCE);
        }

        throw new IllegalArgumentException();
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / EXTRA_STEP) + 1) * EXTRA_FARE);
    }
}
