package nextstep.subway.util.discount;

import nextstep.subway.domain.Fare;

public class AdultPaymentPolicy implements DiscountAgePolicy {

    private static final int BASE_ADULT_AGE = 19;

    @Override
    public void discount(Fare fare) { }

    @Override
    public boolean support(int age) {
        return age >= BASE_ADULT_AGE;
    }
}
