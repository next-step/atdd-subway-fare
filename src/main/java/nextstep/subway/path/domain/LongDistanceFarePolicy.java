package nextstep.subway.path.domain;

public class LongDistanceFarePolicy extends DistanceFarePolicy {
    @Override
    public boolean isSatisfiedBy(int totalDistance) {
        return MEDIUM_DISTANCE_LIMIT < totalDistance;
    }

    @Override
    protected int calculateDistanceFare(int totalDistance) {
        int lastDistance = totalDistance - MEDIUM_DISTANCE_LIMIT;
        int middleDistance = totalDistance - lastDistance - SHORT_DISTANCE_LIMIT;

        return BASIC_FEE + calculateOverFare(middleDistance, MEDIUM_DISTANCE_LIMIT) + calculateOverFare(lastDistance, LONG_DISTANCE_UNIT);
    }
}
