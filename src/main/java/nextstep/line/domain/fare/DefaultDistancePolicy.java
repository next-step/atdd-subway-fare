package nextstep.line.domain.fare;

public class DefaultDistancePolicy implements DistanceFarePolicy {

    private static final int MAX_DISTANCE = 10;
    private static final int MIN_DISTANCE = 0;

    @Override
    public boolean isIncluded(int distance) {
        return distance > MIN_DISTANCE && distance <= MAX_DISTANCE;
    }

    @Override
    public int fare(int distance) {
        return DEFAULT_FARE;
    }
}
