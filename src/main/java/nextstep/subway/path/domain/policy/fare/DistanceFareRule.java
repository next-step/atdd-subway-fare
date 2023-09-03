package nextstep.subway.path.domain.policy.fare;

public abstract class DistanceFareRule {
    protected static final int BASIC_FEE = 1250;
    protected static final int SHORT_DISTANCE_LIMIT = 10;
    protected static final int MEDIUM_DISTANCE_LIMIT = 50;
    protected static final int MEDIUM_DISTANCE_UNIT = 5;
    protected static final int LONG_DISTANCE_UNIT = 8;
    protected static final int OVER_FARE = 100;

    abstract boolean isSatisfiedBy(int totalDistance);

    abstract int calculateDistanceFare(int totalDistance);

    protected int getMiddleDistance(int totalDistance) {
        if (totalDistance <= MEDIUM_DISTANCE_LIMIT) {
            return totalDistance - SHORT_DISTANCE_LIMIT;
        }

        return MEDIUM_DISTANCE_LIMIT - SHORT_DISTANCE_LIMIT;
    }

    protected int getLastDistance(int totalDistance) {
        return totalDistance - MEDIUM_DISTANCE_LIMIT;
    }

    protected int calculateOverFare(int distance, int distanceUnit) {
        return ((distance - 1) / distanceUnit + 1) * OVER_FARE;
    }
}
