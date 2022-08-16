package nextstep.path.domain.fare;

public class BaseFarePolicy extends FarePolicy {

    private static final int BASE_FARE = 1250;

    public BaseFarePolicy(FarePolicy nextPolicy) {
        super(nextPolicy);
    }

    @Override
    public int apply(int beforeFare) {
        return nextPolicy.apply(BASE_FARE);
    }
}
