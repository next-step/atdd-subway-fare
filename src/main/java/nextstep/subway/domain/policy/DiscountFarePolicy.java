package nextstep.subway.domain.policy;

public class DiscountFarePolicy extends FarePolicy {
    private DiscountType discountType;

    public DiscountFarePolicy(int age, FarePolicy farePolicy) {
        super(farePolicy);
        this.discountType = DiscountType.of(age);
    }

    public DiscountFarePolicy(DiscountType discountType, FarePolicy farePolicy) {
        super(farePolicy);
        this.discountType = discountType;
    }

    @Override
    public int calculate() {
        return discountType.discount(super.calculate());
    }
}
