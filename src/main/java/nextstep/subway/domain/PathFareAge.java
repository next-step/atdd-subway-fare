package nextstep.subway.domain;

import nextstep.member.domain.LoginMember;

public class PathFareAge {

    private PathFareAge() {}

    public static Fare of(final LoginMember loginMember, final Fare usageFare) {
        return calculatorFare(loginMember, usageFare);
    }

    private static Fare calculatorFare(final LoginMember loginMember, final Fare fare) {
        if (loginMember.isGuest()) {
            return fare;
        }
        return AgeFarePolicy.of(loginMember, fare);
    }
}
