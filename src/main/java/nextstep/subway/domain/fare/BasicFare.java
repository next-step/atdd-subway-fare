package nextstep.subway.domain.fare;

public class BasicFare implements FarePolicy {

    private static final int BASIC_FARE = 1250;
    private final int addtionalFare;
    private FarePolicy nextPolicyChain;

    public BasicFare(final int addtionalFare) {
        this.addtionalFare = addtionalFare;
    }

    @Override
    public void setNextPolicyChain(final FarePolicy farePolicy) {
        nextPolicyChain = farePolicy;
    }

    @Override
    public int calculateFare(final int distance) {
        return BASIC_FARE + addtionalFare + nextPolicyChain.calculateFare(distance);
    }
}
