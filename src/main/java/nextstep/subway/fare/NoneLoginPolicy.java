package nextstep.subway.fare;

public class NoneLoginPolicy extends DiscountPolicy {

    public NoneLoginPolicy(ExtraFarePolicy... extraFarePolicies) {
        super(extraFarePolicies);
    }

    @Override
    protected int getDiscountAmount(int fare) {
        return fare;
    }
}
