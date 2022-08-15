package nextstep.subway.domain.policy.discount;

import nextstep.member.domain.Member;

public class NotDiscount implements DiscountPolicy {

    @Override
    public boolean supports(Member member) {
        return false;
    }

    @Override
    public int discount(int fare) {
        return fare;
    }
}
