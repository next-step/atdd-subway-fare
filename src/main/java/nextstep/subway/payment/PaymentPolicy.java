package nextstep.subway.payment;

public interface PaymentPolicy {

    void pay(PaymentRequest paymentRequest);
}
