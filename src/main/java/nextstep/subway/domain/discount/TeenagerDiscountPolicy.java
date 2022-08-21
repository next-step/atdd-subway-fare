package nextstep.subway.domain.discount;

import org.springframework.stereotype.Component;

@Component
public class TeenagerDiscountPolicy implements DiscountPolicy {

    private static final int ABSOLUTE_DISCOUNT = 350;
    private static final float DISCOUNT_RATIO = 0.2f;

    @Override
    public boolean support(int age) {
        return age >= 13 && age < 19;
    }

    @Override
    public int discount(int fare) {
        return (int) ((fare - ABSOLUTE_DISCOUNT) * (1 - DISCOUNT_RATIO));
    }

}
