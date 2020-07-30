package nextstep.subway.maps.map.domain;

import nextstep.subway.maps.line.domain.Money;

import static nextstep.subway.maps.map.domain.DiscountPolicyType.DiscountConstant.DEDUCT_AMOUNT;

public enum DiscountPolicyType {
    CHILDREN(money -> {
        Money discountPrice = money.minus(DEDUCT_AMOUNT).percent(50);
        return money.minus(discountPrice);
    }),
    YOUTH(money -> {
        Money deductedPrice = money.minus(DEDUCT_AMOUNT);
        Money discountPrice = deductedPrice.percent(20);
        return money.minus(discountPrice);
    }),
    ADULT(money -> money);

    public static final int MAXIMUM_CHILDREN_AGE = 12;
    public static final int MINIMUM_CHILDREN_AGE = 6;
    public static final int MAXIMUM_YOUTH_AGE = 18;
    private final DiscountPolicy discountPolicy;

    DiscountPolicyType(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public static DiscountPolicy ofAge(int age) {
        if (age <= MAXIMUM_CHILDREN_AGE && age >= MINIMUM_CHILDREN_AGE) {
            return CHILDREN.getDiscountPolicy();
        }
        if (age > MINIMUM_CHILDREN_AGE && age <= MAXIMUM_YOUTH_AGE) {
            return YOUTH.getDiscountPolicy();
        }

        if (age > MAXIMUM_YOUTH_AGE) {
            return ADULT.getDiscountPolicy();
        }
        throw new RuntimeException("가입이 불가능한 나이 입니다.");
    }

    protected DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    static class DiscountConstant {
        public static final Money DEDUCT_AMOUNT = Money.wons(350);
    }
}


