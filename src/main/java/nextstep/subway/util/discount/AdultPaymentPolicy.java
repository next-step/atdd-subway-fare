package nextstep.subway.util.discount;

import nextstep.subway.domain.Fare;

public class AdultPaymentPolicy implements DiscountAgePolicy {

    @Override
    public void discount(Fare fare) { }

    @Override
    public boolean support(int age) {
        return age >= 19;
    }
}
