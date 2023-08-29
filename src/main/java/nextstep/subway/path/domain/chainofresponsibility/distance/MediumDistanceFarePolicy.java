package nextstep.subway.path.domain.chainofresponsibility.distance;

public class MediumDistanceFarePolicy extends DistanceFarePolicy {
    @Override
    protected boolean isSatisfiedBy(int totalDistance) {
        return SHORT_DISTANCE_LIMIT < totalDistance
                && totalDistance <= MEDIUM_DISTANCE_LIMIT;
    }

    @Override
    protected int calculateDistanceFare(int totalDistance) {
        int lastDistance = totalDistance - SHORT_DISTANCE_LIMIT;
        return BASIC_FEE + calculateOverFare(lastDistance, MEDIUM_DISTANCE_UNIT);
    }
}
