package nextstep.subway.path.domain.policy.discount;

import nextstep.subway.path.domain.policy.DiscountPolicy;

public class NoDiscount implements DiscountPolicy {

    @Override
    public int discount(int fare) {
        return fare;
    }
}
