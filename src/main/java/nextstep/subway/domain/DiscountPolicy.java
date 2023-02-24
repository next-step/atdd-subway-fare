package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum DiscountPolicy {
    ADULT(
        age -> age >= DiscountPolicy.ADULT_AGE,
        fare -> 0
    ),
    TEENAGER(
        age -> age >= DiscountPolicy.TEENAGER_MIN_AGE && age <= DiscountPolicy.TEENAGER_MAX_AGE,
        fare -> (int)((fare - DiscountPolicy.DEDUCTED_AMOUNT) * DiscountPolicy.TEENAGER_DISCOUNT_RATE)
    ),
    CHILD(
        age -> age >= DiscountPolicy.CHILD_MIN_AGE && age <= DiscountPolicy.CHILD_MAX_AGE,
        fare -> (int)((fare - DiscountPolicy.DEDUCTED_AMOUNT) * DiscountPolicy.CHILD_DISCOUNT_RATE)
    );

    private static final int ADULT_AGE = 20;
    private static final int TEENAGER_MIN_AGE = 13;
    private static final int TEENAGER_MAX_AGE = 18;
    private static final int CHILD_MIN_AGE = 6;
    private static final int CHILD_MAX_AGE = 12;
    private static final int DEDUCTED_AMOUNT = 350;
    private static final double TEENAGER_DISCOUNT_RATE = 0.2;
    private static final double CHILD_DISCOUNT_RATE = 0.5;

    private final Predicate<Integer> predicate;
    private final UnaryOperator<Integer> operator;

    DiscountPolicy(Predicate<Integer> predicate, UnaryOperator<Integer> operator) {
        this.predicate = predicate;
        this.operator = operator;
    }

    public static int discount(int fare, int age) {
        int discountAmount = Arrays.stream(values())
            .filter(policy -> policy.predicate.test(age))
            .findFirst()
            .orElse(ADULT)
            .operator.apply(fare);
        return fare - discountAmount;
    }
}
