package nextstep.subway.domain.discount;

import org.springframework.stereotype.Component;

@Component
public class ChildrenDiscountPolicy implements DiscountPolicy {

    private static final int ABSOLUTE_DISCOUNT = 350;
    private static final float DISCOUNT_RATIO = 0.5f;

    @Override
    public boolean support(int age) {
        return age >= 6 && age < 13;
    }

    @Override
    public int discount(int fare) {
        return (int) ((fare - ABSOLUTE_DISCOUNT) * (1 - DISCOUNT_RATIO));
    }

}
