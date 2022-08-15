package nextstep.subway.domain.policy.discount;

import nextstep.member.domain.Member;

public class ChildrenDiscount implements DiscountPolicy {

    @Override
    public boolean supports(Member member) {
        return member.isChildren();
    }

    @Override
    public int discount(int fare) {
        return 0;
    }
}
