package nextstep.subway.domain;

public class FareCalculator {

    public static final int FARE_PER_INTERVAL = 100;
    public static final int DISTANCE_INTERVAL_KM = 5;

    private FareCalculator() {
        throw new IllegalStateException();
    }

    public static int calculateByDistance(int distance) {
        return ((distance / DISTANCE_INTERVAL_KM) + calculateAdditionalCharge(distance)) * FARE_PER_INTERVAL;
    }

    private static int calculateAdditionalCharge(int distance) {
        return (distance % DISTANCE_INTERVAL_KM) > 0 ? 1 : 0;
    }
}
