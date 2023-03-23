package nextstep.subway.domain.fare;

public class ThirdFarePolicy implements FarePolicy {

    private static final int KM = 8;
    private static final int PREVIOUS_DISTANCE_THRESHOLDS = 50;
    private FarePolicy nextPolicyChain;

    @Override
    public void setNextPolicyChain(final FarePolicy farePolicy) {
        nextPolicyChain = farePolicy;
    }

    @Override
    public int calculateFare(final int distance) {
        return calculateOverFareMoreThan50km(distance - PREVIOUS_DISTANCE_THRESHOLDS);
    }


    private static int calculateOverFareMoreThan50km(int distance) {
        return (int) ((Math.ceil((distance - 1) / KM) + 1) * 100);
    }
}
