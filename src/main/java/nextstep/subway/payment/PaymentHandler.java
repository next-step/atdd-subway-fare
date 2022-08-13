package nextstep.subway.payment;


import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Path;
import nextstep.subway.util.discount.DiscountAgePolicy;

import java.util.ArrayList;
import java.util.List;

public class PaymentHandler {

    private final List<PaymentPolicy> paymentPolicyList = new ArrayList<>();

    public PaymentHandler(Path path, DiscountAgePolicy discountAgePolicy) {
        paymentPolicyList.add(new DistancePaymentPolicy(path.extractDistance()));
        paymentPolicyList.add(new LinePaymentPolicy(path.getSections().getSections()));
        paymentPolicyList.add(new AgeDiscountPaymentPolicy(discountAgePolicy));
    }

    public void calculate(Fare fare) {
        paymentPolicyList.forEach(paymentPolicy -> paymentPolicy.calculate(fare));
    }

}
