package nextstep.subway.payment;

import nextstep.subway.util.discount.DiscountAgePolicyFactory;
import nextstep.subway.util.discount.DiscountAgePolicy;

public class AgeDiscountPaymentPolicy implements PaymentPolicy {

    private final DiscountAgePolicyFactory discountAgePolicyFactory;

    public AgeDiscountPaymentPolicy(DiscountAgePolicyFactory discountAgePolicyFactory) {
        this.discountAgePolicyFactory = discountAgePolicyFactory;
    }

    @Override
    public void pay(PaymentRequest paymentRequest) {
        DiscountAgePolicy discountAgePolicy = discountAgePolicyFactory.findUsersAge(paymentRequest.getLoginMemberAge());
        discountAgePolicy.discount(paymentRequest.getFare());
    }
}
