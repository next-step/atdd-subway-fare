package nextstep.subway.domain.policy.over;

public class Over10FarePolicy extends OverFarePolicy {
    private static final int OVER_DISTANCE = 10;
    private static final int DISTANCE_PER = 5;

    public Over10FarePolicy(OverFarePolicy next) {
        super(next);
    }

    @Override
    protected int calculateFare(int distance) {
        return calculateOverFare(distance, DISTANCE_PER);
    }

    @Override
    protected boolean isOverFare(int distance) {
        return OVER_DISTANCE < distance;
    }
}
