package nextstep.subway.payment;

import nextstep.subway.domain.Fare;

@FunctionalInterface
public interface PaymentPolicy {

    void calculate(Fare fare);
}
