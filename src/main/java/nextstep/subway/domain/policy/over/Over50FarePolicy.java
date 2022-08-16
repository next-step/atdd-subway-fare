package nextstep.subway.domain.policy.over;

public class Over50FarePolicy extends OverFarePolicy {
    private static final int OVER_DISTANCE = 50;
    private static final int DISTANCE_PER = 8;

    public Over50FarePolicy(OverFarePolicy next) {
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
