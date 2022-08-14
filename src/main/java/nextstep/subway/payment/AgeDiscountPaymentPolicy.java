package nextstep.subway.payment;

import nextstep.subway.util.discount.AgeFactory;
import nextstep.subway.util.discount.DiscountAgePolicy;

public class AgeDiscountPaymentPolicy implements PaymentPolicy {

    private final AgeFactory ageFactory;

    public AgeDiscountPaymentPolicy(AgeFactory ageFactory) {
        this.ageFactory = ageFactory;
    }

    @Override
    public void pay(PaymentRequest paymentRequest) {
        DiscountAgePolicy discountAgePolicy = ageFactory.findUsersAge(paymentRequest.getLoginMemberAge());
        discountAgePolicy.discount(paymentRequest.getFare());
    }
}
