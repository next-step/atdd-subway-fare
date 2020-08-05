package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Money;

public enum DiscountPolicyType {

    CHILDREN(money -> money.minus(Discount.BASE_DISCOUNT).percentOff(20)),
    YOUTH(money -> money.minus(Discount.BASE_DISCOUNT).percentOff(50)),
    ADULT(money -> money);

    public static final int MAXIMUM_CHILDREN_AGE_BOUND = 12;
    public static final int MINIMUM_CHILDREN_AGE_BOUND = 6;
    public static final int MAXIMUM_YOUTH_AGE_BOUND = 18;
    public static final int BASE_DISCOUNT_VALUE = 350;

    private final DiscountPolicy discountPolicy;

    DiscountPolicyType(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public static DiscountPolicyType ofAge(int age) {
        if (age <= MAXIMUM_CHILDREN_AGE_BOUND && age >= MINIMUM_CHILDREN_AGE_BOUND) {
            return CHILDREN;
        }
        if (age > MINIMUM_CHILDREN_AGE_BOUND && age <= MAXIMUM_YOUTH_AGE_BOUND) {
            return YOUTH;
        }
        if (age > MAXIMUM_YOUTH_AGE_BOUND) {
            return ADULT;
        }
        throw new IllegalStateException("wrong age typed");
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    private static class Discount {
        public static final Money BASE_DISCOUNT = Money.drawNewMoney(BASE_DISCOUNT_VALUE);
    }
}
