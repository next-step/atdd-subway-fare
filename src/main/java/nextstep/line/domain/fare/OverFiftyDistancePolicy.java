package nextstep.line.domain.fare;

public class OverFiftyDistancePolicy extends OverTenDistancePolicy implements DistanceFarePolicy {

    private static final int MIN_DISTANCE = 50;
    private static final int DISTANCE_INTERVAL = 8;

    @Override
    public boolean isIncluded(int distance) {
        return distance > MIN_DISTANCE;
    }

    @Override
    public int fare(int distance) {
        return DEFAULT_FARE
                + extraCharges(40, 5)
                + extraCharges(distance - MIN_DISTANCE, DISTANCE_INTERVAL);
    }


}
