package nextstep.subway.domain.policy;

public class OverFiftyExtraFare implements FarePolicy {

    private static final int MINIMUM_DISTANCE = 50;
    private static final int EXTRA_FARE = 100;
    private static final int EXTRA_UNIT = 8;

    @Override
    public boolean supports(int distance) {
        return distance > MINIMUM_DISTANCE;
    }

    @Override
    public int fare(int distance) {
        return (int) (Math.ceil((double) (distance - MINIMUM_DISTANCE) / EXTRA_UNIT) * EXTRA_FARE);
    }
}
