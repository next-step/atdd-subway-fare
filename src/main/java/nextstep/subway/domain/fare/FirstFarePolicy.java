package nextstep.subway.domain.fare;

public class FirstFarePolicy implements FarePolicy {

    private static final int DISTANCE_THRESHOLDS = 10;
    private static final int BASIC_FARE = 1250;
    private FarePolicy nextPolicyChain;

    @Override
    public void setNextPolicyChain(final FarePolicy farePolicy) {
        nextPolicyChain = farePolicy;
    }

    @Override
    public int calculateFare(final int distance) {
        if (distance > DISTANCE_THRESHOLDS) {
            return BASIC_FARE
                    + nextPolicyChain.calculateFare(distance);
        }

        return BASIC_FARE;
    }
}
