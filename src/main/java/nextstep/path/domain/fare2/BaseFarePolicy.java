package nextstep.path.domain.fare2;

public class BaseFarePolicy extends FarePolicy {

    public BaseFarePolicy(FarePolicy nextPolicy) {
        super(nextPolicy);
    }

    @Override
    public int apply(int beforeFare) {
        return nextPolicy.apply(1250);
    }
}
