package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum Age {
    CHILDREN(
        age -> 6 <= age && age < 13,
        fare -> Integer.max(fare - Age.BASE_DISCOUNT_FARE, 0) / 10 * 5
    ),
    TEENAGER(
        age -> 13 <= age && age < 19,
        fare -> Integer.max(fare - Age.BASE_DISCOUNT_FARE, 0) / 10 * 8
    ),
    ADULT(
        age -> age >= 19,
        fare -> fare
    );

    private static final int BASE_DISCOUNT_FARE = 350;

    private Predicate<Integer> condition;
    private UnaryOperator<Integer> operator;

    Age(Predicate<Integer> condition, UnaryOperator<Integer> operator) {
        this.condition = condition;
        this.operator = operator;
    }

    public static Age of(int intAge) {
        return Arrays.stream(values())
            .filter(age -> satisfyAgeCondition(age, intAge))
            .findFirst()
            .orElse(ADULT);
    }

    private static boolean satisfyAgeCondition(Age age, int intAge) {
        return age.condition.test(intAge);
    }

    public int discountFare(int fare) {
        return operator.apply(fare);
    }
}
