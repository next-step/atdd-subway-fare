package nextstep.subway.path.domain.fare.distance;

public abstract class DefaultDistanceFarePolicy implements DistanceFarePolicy {
    protected static final int BASIC_FEE = 1250;
    protected static final int SHORT_DISTANCE_LIMIT = 10;
    protected static final int MIDDLE_DISTANCE_LIMIT = 50;
    protected static final int MIDDLE_DISTANCE_UNIT = 5;
    protected static final int LONG_DISTANCE_UNIT = 8;
    private static final int OVER_FARE = 100;

    protected int calculateOverFare(int distance, int distanceUnit) {
        return ((distance - 1) / distanceUnit + 1) * OVER_FARE;
    }
}
