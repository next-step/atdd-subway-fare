package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntPredicate;

import static nextstep.subway.domain.Fare.AGE_DISCOUNT_EXEMPTION_FARE;

public enum AgeDiscountPolicy {
    CHILDREN_DISCOUNT(50, age -> age >= 6 && age < 13),
    TEENAGER_DISCOUNT(20, age -> age >= 13 && age < 19),
    ELSE(0, age -> age < 6 || age >= 20);

    private int discountPercent;
    private IntPredicate ageDiscountCondition;

    AgeDiscountPolicy(int discountPercent, IntPredicate ageDiscountCondition) {
        this.discountPercent = discountPercent;
        this.ageDiscountCondition = ageDiscountCondition;
    }

    public static Fare calculateAgeDiscountFare(Fare fare, int age) {
        int discountPercent = Arrays.stream(values())
                .filter(it -> it.ageDiscountCondition.test(age))
                .findFirst().orElseThrow(() -> new IllegalStateException("[NEED HOTFIX] 나이에 해당하는 enum이 없습니다. age: " + age))
                .discountPercent;

        return fare.minus(AGE_DISCOUNT_EXEMPTION_FARE).ofPercent(discountPercent);
    }
}
