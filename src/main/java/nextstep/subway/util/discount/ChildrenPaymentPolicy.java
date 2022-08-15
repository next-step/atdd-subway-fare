package nextstep.subway.util.discount;

import nextstep.subway.domain.Fare;

public class ChildrenPaymentPolicy implements DiscountAgePolicy {

    private static final double DISCOUNT_RATE = 0.5;
    private static final int BASE_CHILDREN_AGE = 6;
    private static final int UPPER_CHILDREN_AGE = 13;


    @Override
    public void discount(Fare fare) {
        fare.decrease((int) ((fare.fare() - DEDUCTION_FARE) * DISCOUNT_RATE));
    }

    @Override
    public boolean support(int age) {
        return age >= BASE_CHILDREN_AGE && age < UPPER_CHILDREN_AGE;
    }
}
