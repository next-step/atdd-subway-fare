package nextstep.subway.payment;

import nextstep.subway.domain.Fare;

public interface PaymentRequest extends PathInformation, LoginMemberInformation {

    void addFare(int addedFare);

    Fare getFare();
}
