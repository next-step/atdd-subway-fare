package nextstep.subway.util;

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
