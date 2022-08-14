package nextstep.subway.payment;

public class LinePaymentPolicy implements PaymentPolicy {

    public LinePaymentPolicy() { }

    @Override
    public void pay(PaymentRequest paymentRequest) {
        int mostExpensiveLineFare = paymentRequest.getPathResult().mostExpensiveLineFare();
        paymentRequest.addFare(mostExpensiveLineFare);
    }
}
