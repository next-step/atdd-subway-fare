package nextstep.subway.domain.policy.discount;

import nextstep.member.domain.Member;

public interface DiscountPolicy {

    int NOT_DISCOUNT_FARE = 350;

    boolean supports(Member member);

    int discount(int fare);
}
