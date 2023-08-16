package nextstep.line.domain.fare;

public class OverTenDistancePolicy implements DistanceFarePolicy {

    private static final int MAX_DISTANCE = 50;
    private static final int MIN_DISTANCE = 10;
    private static final int DISTANCE_INTERVAL = 5;

    @Override
    public boolean isIncluded(int distance) {
        return MIN_DISTANCE < distance && distance <= MAX_DISTANCE;
    }

    @Override
    public int fare(int distance) {
        return DEFAULT_FARE + extraCharges(distance - MIN_DISTANCE, DISTANCE_INTERVAL);
    }
}
