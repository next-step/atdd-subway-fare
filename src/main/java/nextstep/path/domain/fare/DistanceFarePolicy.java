package nextstep.path.domain.fare;

public class DistanceFarePolicy implements FarePolicy {
    private static final int BASE_FARE_DISTANCE = 10;
    private static final int LONG_DISTANCE = 50;

    private static final int BASE_FARE = 1250;
    private static final int BONUS_FARE = 100;

    private static final int SHORT_DISTANCE_INTERVAL_FOR_BONUS = 5;
    private static final int LONG_DISTANCE_INTERVAL_FOR_BONUS = 8;

    private final int distance;

    public DistanceFarePolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int apply(int beforeFare) {
        return beforeFare + calculateFare(distance);
    }

    private int calculateFare(int distance) {
        return BASE_FARE + calculateOverFare(distance);
    }

    private int calculateOverFare(int distance) {
        if (distance <= BASE_FARE_DISTANCE) {
            return 0;
        }

        if (distance <= LONG_DISTANCE) {
            return calculateOverFare(distance - BASE_FARE_DISTANCE, SHORT_DISTANCE_INTERVAL_FOR_BONUS);
        }

        return calculateOverFare(LONG_DISTANCE - (BASE_FARE_DISTANCE + 1), SHORT_DISTANCE_INTERVAL_FOR_BONUS)
        + calculateOverFare(distance - LONG_DISTANCE, LONG_DISTANCE_INTERVAL_FOR_BONUS);
    }

    private int calculateOverFare(int overDistance, int distanceInterval) {
        int overTimes = (int) Math.ceil((overDistance + 1d) / distanceInterval);
        return overTimes * BONUS_FARE;
    }
}
