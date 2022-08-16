package nextstep.path.domain.fare;

public class DistanceExtraFarePolicy extends FarePolicy {

    private static final int BASE_FARE_DISTANCE = 10;
    private static final int LONG_DISTANCE = 50;

    private static final int BONUS_FARE = 100;

    private static final int SHORT_DISTANCE_INTERVAL_FOR_BONUS = 5;
    private static final int LONG_DISTANCE_INTERVAL_FOR_BONUS = 8;

    private final int distance;

    DistanceExtraFarePolicy(int distance, FarePolicy nextPolicy) {
        super(nextPolicy);
        this.distance = distance;
    }

    @Override
    public int apply(int beforeFare) {
        return nextPolicy.apply(beforeFare + extraFare(distance));
    }

    private int extraFare(int distance) {
        if (distance <= BASE_FARE_DISTANCE) {
            return 0;
        }

        if (distance <= LONG_DISTANCE) {
            return extraFare(distance - BASE_FARE_DISTANCE, SHORT_DISTANCE_INTERVAL_FOR_BONUS);
        }

        return extraFare(LONG_DISTANCE - (BASE_FARE_DISTANCE + 1), SHORT_DISTANCE_INTERVAL_FOR_BONUS)
                + extraFare(distance - LONG_DISTANCE, LONG_DISTANCE_INTERVAL_FOR_BONUS);
    }

    private int extraFare(int overDistance, int distanceInterval) {
        int overTimes = (int) Math.ceil((overDistance + 1d) / distanceInterval);
        return overTimes * BONUS_FARE;
    }
}
