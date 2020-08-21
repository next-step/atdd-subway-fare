package nextstep.subway.fare;

public class ChildPolicy extends DiscountPolicy {
    private static final double DISCOUNT_PERCENT = 50.0 / 100;
    private static final int BASIC_RATE = 350;

    public ChildPolicy(ExtraFarePolicy... extraFarePolicies) {
        super(extraFarePolicies);
    }

    @Override
    protected int getDiscountAmount(int fare) {
        return (int) ((fare - BASIC_RATE) * DISCOUNT_PERCENT);
    }

}
