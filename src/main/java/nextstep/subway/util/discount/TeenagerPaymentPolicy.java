package nextstep.subway.util.discount;

import nextstep.subway.domain.Fare;

public class TeenagerPaymentPolicy implements DiscountAgePolicy {

    private static final double DISCOUNT_RATE = 0.2;
    private static final int BASE_TEENAGER_AGE = 13;
    private static final int UPPER_TEENAGER_AGE = 19;

    @Override
    public void discount(Fare fare) {
        fare.decrease((int) ((fare.fare() - DEDUCTION_FARE) * DISCOUNT_RATE));
    }

    @Override
    public boolean support(int age) {
        return age >= BASE_TEENAGER_AGE && age < UPPER_TEENAGER_AGE;
    }
}
