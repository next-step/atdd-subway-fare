package nextstep.subway.domain.fare;

public class SecondFarePolicy implements FarePolicy {

    private static final int KM = 5;
    private static final int DISTANCE_THRESHOLDS = 50;
    private static final int PREVIOUS_DISTANCE_THRESHOLDS = 10;
    private FarePolicy nextPolicyChain;

    @Override
    public void setNextPolicyChain(final FarePolicy farePolicy) {
        nextPolicyChain = farePolicy;
    }

    @Override
    public int calculateFare(final int distance) {
        if (distance > DISTANCE_THRESHOLDS) {
            return calculateFare(DISTANCE_THRESHOLDS)
                    + nextPolicyChain.calculateFare(distance);
        }

        return calculateOverFareLessThan50km(distance - PREVIOUS_DISTANCE_THRESHOLDS);
    }


    private static int calculateOverFareLessThan50km(int distance) {
        return (int) ((Math.ceil((distance - 1) / KM) + 1) * 100);
    }
}
