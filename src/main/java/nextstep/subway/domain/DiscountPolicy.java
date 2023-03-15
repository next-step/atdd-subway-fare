package nextstep.subway.domain;

import nextstep.member.domain.LoginMember;

public interface DiscountPolicy {

    int calculator(int fare, LoginMember member);
}
