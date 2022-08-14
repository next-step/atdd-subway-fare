package nextstep.subway.domain.policy;

public class ElevenToFiftyExtraFare implements FarePolicy {

    private static final int MINIMUM_DISTANCE = 10;
    private static final int MAXIMUM_DISTANCE = 50;
    private static final int EXTRA_FARE = 100;
    private static final int EXTRA_UNIT = 5;
    private static final int FULL_FARE = 800;

    @Override
    public boolean supports(int distance) {
        return distance > MINIMUM_DISTANCE;
    }

    @Override
    public int fare(int distance) {
        if (distance >= MAXIMUM_DISTANCE) {
            return FULL_FARE;
        }
        return (int) (Math.ceil((double) (distance - MINIMUM_DISTANCE) / EXTRA_UNIT) * EXTRA_FARE);
    }
}
