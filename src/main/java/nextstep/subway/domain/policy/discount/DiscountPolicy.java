package nextstep.subway.domain.policy.discount;

import nextstep.member.domain.Member;

public interface DiscountPolicy {

    boolean supports(Member member);

    int discount(int fare);
}
