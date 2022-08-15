package nextstep.subway.applicaion.discount;

import nextstep.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class ChildRateDiscountPolicy implements DiscountPolicy {

    private static final int MIN_AGE = 6;
    private static final int MAX_AGE = 13;
    private static final int DEFAULT_DISCOUNT_AMOUNT = 350;
    private static final double DISCOUNT_RATE = 0.5;

    @Override
    public boolean isTarget(final Member member) {
        return MIN_AGE <= member.getAge()
                && member.getAge() < MAX_AGE;
    }

    @Override
    public int discount(final int fare) {
        return (int) ((fare - DEFAULT_DISCOUNT_AMOUNT) * DISCOUNT_RATE);
    }
}
