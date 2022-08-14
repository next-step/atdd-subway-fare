package nextstep.subway.util.discount;

import nextstep.subway.domain.Fare;

public class ChildrenPaymentPolicy implements DiscountAgePolicy {

    private static final double DISCOUNT_RATE = 0.5;

    @Override
    public void discount(Fare fare) {
        fare.decrease((int) ((fare.fare() - DEDUCTION_FARE) * DISCOUNT_RATE));
    }

    @Override
    public boolean support(int age) {
        return age >= 6 && age < 13;
    }
}
