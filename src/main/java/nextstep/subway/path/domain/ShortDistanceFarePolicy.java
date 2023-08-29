package nextstep.subway.path.domain;

public class ShortDistanceFarePolicy extends DistanceFarePolicy {
    @Override
    protected boolean isSatisfiedBy(int totalDistance) {
        return totalDistance <= SHORT_DISTANCE_LIMIT;
    }

    @Override
    protected int calculateDistanceFare(int totalDistance) {
        return BASIC_FEE;
    }
}
