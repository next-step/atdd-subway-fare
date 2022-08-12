package nextstep.subway.util;

import nextstep.subway.util.discount.DiscountAgePolicy;

public class UserDiscountPolicyByAge implements DiscountPolicy {

    private DiscountAgePolicy discountAgePolicy;

    public UserDiscountPolicyByAge(DiscountAgePolicy discountAgePolicy) {
        this.discountAgePolicy = discountAgePolicy;
    }

    @Override
    public int discount(int fare) {
        return discountAgePolicy.discount(fare);
    }
}
