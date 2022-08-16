package nextstep.subway.domain.policy.discount;

import nextstep.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class ChildrenDiscount implements DiscountPolicy {

    private static final double DISCOUNT_RATE = 0.5;

    @Override
    public boolean supports(Member member) {
        return member.isChildren();
    }

    @Override
    public int discount(int fare) {
        return (int) ((fare - NOT_DISCOUNT_FARE) * DISCOUNT_RATE + NOT_DISCOUNT_FARE);
    }
}
