package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountPolicy {
    STANDARD(age -> false, DiscountPolicy.STANDARD_DISCOUNT_RATE, DiscountPolicy.STANDARD_DEDUCT),
    CHILD(age -> age >= DiscountPolicy.CHILD_MIN_AGE && age < DiscountPolicy.CHILD_MAX_AGE, DiscountPolicy.CHILD_DISCOUNT_RATE, DiscountPolicy.CHILD_DEDUCT),
    TEENAGER(age -> age >= DiscountPolicy.TEENAGER_MIN_AGE && age < DiscountPolicy.TEENAGER_MAX_AGE, DiscountPolicy.TEENAGER_DISCOUNT_RATE, DiscountPolicy.TEENAGER_DEDUCT);
    private final Predicate<Integer> predicate;
    private final int discountRate;
    private final int deduct;
    private final static int STANDARD_DISCOUNT_RATE = 0;
    private final static int STANDARD_DEDUCT = 6;
    private final static int CHILD_MIN_AGE = 6;
    private final static int CHILD_DEDUCT = 350;
    private final static int CHILD_MAX_AGE = 13;
    private final static int CHILD_DISCOUNT_RATE = 50;
    private final static int TEENAGER_MIN_AGE = 13;
    private final static int TEENAGER_MAX_AGE = 19;
    private final static int TEENAGER_DEDUCT = 350;
    private final static int TEENAGER_DISCOUNT_RATE = 50;

    DiscountPolicy(Predicate<Integer> predicate, int discountRate, int deduct) {
        this.predicate = predicate;
        this.discountRate = discountRate;
        this.deduct = deduct;
    }

    public static int calculate(int age, int fare) {
        if (isStandard(age)) {
            return fare;
        }
        return calculateFare(fare, findType(age));
    }

    private static boolean isStandard(int distance) {
        return findType(distance).equals(STANDARD);
    }

    private static int calculateFare(int fare, DiscountPolicy discountPolicy) {
        int deductFare = fare - discountPolicy.deduct;
        return (int) (deductFare - (deductFare) * discountPolicy.discountRate * 0.01);
    }

    private static DiscountPolicy findType(int age) {
        return Arrays.stream(DiscountPolicy.values())
                .filter(discountPolicy -> discountPolicy.match(age))
                .findFirst()
                .orElse(STANDARD);
    }

    private boolean match(int age) {
        return predicate.test(age);
    }
}
