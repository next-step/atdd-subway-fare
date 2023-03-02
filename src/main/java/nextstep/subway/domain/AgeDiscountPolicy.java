package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum AgeDiscountPolicy {
    CHILDREN_DISCOUNT(50, age -> age >= 6 && age < 13),
    TEENAGER_DISCOUNT(20, age -> age >= 13 && age < 19),
    NO_DISCOUNT(0, age -> age >= 20);

    private int discountRate;
    private Function<Integer, Boolean> ageDiscountCondition;

    AgeDiscountPolicy(int discountRate, Function<Integer, Boolean> ageDiscountCondition) {
        this.discountRate = discountRate;
        this.ageDiscountCondition = ageDiscountCondition;
    }

    public static Fare calculateAgeDiscountFare(Fare fare, int age) {
        int discountRate = Arrays.stream(values())
                .filter(it -> it.ageDiscountCondition.apply(age))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(""))
                .discountRate;

        return Fare.of((fare.getValue() - 350) * discountRate / 100);
    }
}
