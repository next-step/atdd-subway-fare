package nextstep.subway.path.domain.policy.discount;

import org.springframework.stereotype.Component;

@Component
public class TeenagerDiscountPolicy extends AgeDiscountPolicy {
    @Override
    public int discount(int totalFare) {
        return (int) (DEDUCTION_AMOUNT + (totalFare - DEDUCTION_AMOUNT) * 0.8);
    }
}
