package nextstep.subway.payment;

import nextstep.subway.domain.Fare;
import nextstep.subway.util.discount.DiscountAgePolicy;

public class AgeDiscountPaymentPolicy implements PaymentPolicy {

    private DiscountAgePolicy discountAgePolicy;

    public AgeDiscountPaymentPolicy(DiscountAgePolicy discountAgePolicy) {
        this.discountAgePolicy = discountAgePolicy;
    }

    @Override
    public void calculate(Fare fare) {
        discountAgePolicy.discount(fare);
    }
}
