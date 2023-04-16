package nextstep.subway.domain.fare;

import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.domain.Fare;

public class AgeDiscountFareHandler extends FareHandler {
    private final MemberResponse loginMember;

    public AgeDiscountFareHandler(FareHandler fareHandler, MemberResponse loginMember) {
        super(fareHandler);
        this.loginMember = loginMember;
    }

    @Override
    public Fare handle(int distance) {
        Fare fare = super.handle(distance);
        if (loginMember == null) {
            return fare;
        }
        if (loginMember.getAge() >= 13 && loginMember.getAge() < 19) {
            Fare result = fare.teenagersDiscountFare();
            return result;
        }
        if (loginMember.getAge() >= 6 && loginMember.getAge() < 13) {
            Fare result = fare.childrenDiscountFare();
            return result;
        }
        return fare;
    }
}
