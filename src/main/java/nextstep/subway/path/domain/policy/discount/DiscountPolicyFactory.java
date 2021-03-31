package nextstep.subway.path.domain.policy.discount;

import nextstep.subway.path.domain.policy.DiscountPolicy;

public class DiscountPolicyFactory {

    public static DiscountPolicy findPolicy(int age) {
        if (age < 13 && age >= 6) {
            return new ChildrenDiscountPolicy();
        }
        if (age < 19) {
            return new YouthDiscountPolicy();
        }
        return new NoDiscount();
    }
}
