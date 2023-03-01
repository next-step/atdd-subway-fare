package nextstep.subway.domain.policy.discount;

import nextstep.subway.domain.policy.calculate.CalculateConditions;

public interface FareDiscountPolicy {
    boolean isSuite(CalculateConditions conditions);
    int discount(int fare);
}
