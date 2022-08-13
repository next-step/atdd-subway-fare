package nextstep.subway.util.discount;

import nextstep.subway.domain.Fare;

public class Teenager implements DiscountAgePolicy {

    private static final double DISCOUNT_RATE = 0.2;

    @Override
    public void discount(Fare fare) {
        fare.decrease((int) ((fare.fare() - DEDUCTION_FARE) * DISCOUNT_RATE));
    }

    @Override
    public boolean support(int age) {
        return age >= 13 && age < 19;
    }
}
