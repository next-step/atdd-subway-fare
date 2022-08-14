package nextstep.subway.payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentHandler {

    private final List<PaymentPolicy> paymentPolicies = new ArrayList<>();

    public void addPaymentPolicy(PaymentPolicy paymentPolicy) {
        paymentPolicies.add(paymentPolicy);
    }

    public void calculate(PaymentRequest paymentRequest) {
        paymentPolicies.forEach(paymentPolicy -> paymentPolicy.pay(paymentRequest));
    }
}
