package nextstep.subway.domain.policy.age;


import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

enum AgeType {
    BABY(AgeType::babyCalculate, age -> age < 6),
    CHILD(AgeType::childCalculate, age -> age >= 6 && age < 13),
    TEENAGER(AgeType::teenagerCalculate, age -> age >= 13 && age < 19),
    ADULT(AgeType::adultCalculate, age -> age == 0 || age >= 19);

    private static final int DEDUCTIBLE_FARE = 350;
    private static final double CHILD_DISCOUNT_RATE = 0.5;
    private static final double TEENAGER_DISCOUNT_RATE = 0.2;

    private final Function<Integer, Integer> expression;
    private final Predicate<Integer> condition;

    AgeType(Function<Integer, Integer> expression, Predicate<Integer> condition) {
        this.expression = expression;
        this.condition = condition;
    }

    private static int babyCalculate(int originFare) {
        return 0;
    }

    private static int childCalculate(int originFare) {
        // 요금 공제
        int fare = originFare - DEDUCTIBLE_FARE;
        // 할인율 적용
        fare *= (1 - CHILD_DISCOUNT_RATE);
        return fare;
    }

    private static int teenagerCalculate(int originFare) {
        // 요금 공제
        int fare = originFare - DEDUCTIBLE_FARE;
        // 할인율 적용
        fare *= (1 - TEENAGER_DISCOUNT_RATE);
        return fare;
    }

    private static int adultCalculate(int originFare) {
        return originFare;
    }

    public static AgeType createFarePolicy(int age) {
        return Arrays.stream(AgeType.values())
                .filter(it -> it.condition.test(age))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public int calculate(int fare) {
        return expression.apply(fare);
    }
}
