package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Money;

public enum DiscountPolicyType {
    CHILDREN(money -> money.minus(Discount.BASE_DISCOUNT).percentOff(20)),
    YOUTH(money -> money.minus(Discount.BASE_DISCOUNT).percentOff(50)),
    ADULT(money -> money);

    public static final int MAXIMUM_CHILDREN_AGE = 12;
    public static final int MINIMUM_CHILDREN_AGE = 6;
    public static final int MAXIMUM_YOUTH_AGE = 18;
    private final DiscountPolicy discountPolicy;

    DiscountPolicyType(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public static DiscountPolicyType ofAge(int age) {
        if (age <= MAXIMUM_CHILDREN_AGE && age >= MINIMUM_CHILDREN_AGE) {
            return CHILDREN;
        }
        if (age > MINIMUM_CHILDREN_AGE && age <= MAXIMUM_YOUTH_AGE) {
            return YOUTH;
        }

        if (age > MAXIMUM_YOUTH_AGE) {
            return ADULT;
        }
        throw new RuntimeException("가입이 불가능한 나이 입니다.");
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    private static class Discount {
        public static final Money BASE_DISCOUNT = Money.wons(350);
    }
}
