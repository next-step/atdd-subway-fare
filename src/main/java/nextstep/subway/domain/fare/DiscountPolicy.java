package nextstep.subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author a1101466 on 2022/08/13
 * @project subway
 * @description
 */
public enum DiscountPolicy {
    CHILD(DiscountPolicy::isChild, DiscountPolicy::calculateChild),
    TEENAGER(DiscountPolicy::isTeenAger, DiscountPolicy::calculateTeenager),
    ADULT(DiscountPolicy::isAdult, DiscountPolicy::calculateAdult)
    ;

    private Predicate<Integer> condition;
    private Function<Integer, Integer> calculator;

    private static final int DEFAULT_DISCOUNT_FARE = 350;

    DiscountPolicy(Predicate<Integer> condition, Function<Integer, Integer> calculator) {
        this.condition = condition;
        this.calculator = calculator;
    }

    public static int calculator(int age , int fare){
        DiscountPolicy discountPolicy = Arrays.stream(values())
                .filter(policy -> policy.condition.test(age))
                .findFirst()
                .orElseThrow();

        return discountPolicy.calculator.apply(fare);
    }

    private static boolean isChild(Integer age) {
        return age >= 6 && age < 13;
    }

    private static boolean isTeenAger(Integer age) {
        return age < 19;
    }

    private static boolean isAdult(Integer age) {
        return age >= 19;
    }

    private static Integer calculateChild(Integer fare) {
        return (int) ((fare - DEFAULT_DISCOUNT_FARE) * 0.5);
    }

    private static Integer calculateTeenager(Integer fare) {
        return (int) ((fare - DEFAULT_DISCOUNT_FARE) * 0.8);
    }

    private static Integer calculateAdult(Integer fare) {
        return fare;
    }
}
