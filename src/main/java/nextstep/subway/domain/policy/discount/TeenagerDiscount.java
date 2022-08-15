package nextstep.subway.domain.policy.discount;

import nextstep.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class TeenagerDiscount implements DiscountPolicy {

    private static final double DISCOUNT_RATE = 0.8;

    @Override
    public boolean supports(Member member) {
        return member.isTeenager();
    }

    @Override
    public int discount(int fare) {
        return (int) ((fare - NOT_DISCOUNT_FARE) * DISCOUNT_RATE + NOT_DISCOUNT_FARE);
    }
}
