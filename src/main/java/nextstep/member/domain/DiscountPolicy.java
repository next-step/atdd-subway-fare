package nextstep.member.domain;

import static nextstep.member.domain.DiscountPolicy.Constants.CHILDREN_DISCOUNT_RATE;
import static nextstep.member.domain.DiscountPolicy.Constants.CHILDREN_MINIMUM_AGE;
import static nextstep.member.domain.DiscountPolicy.Constants.DEFAULT_DEDUCTED_AMOUNT;
import static nextstep.member.domain.DiscountPolicy.Constants.TEENAGER_DISCOUNT_RATE;
import static nextstep.member.domain.DiscountPolicy.Constants.TEENAGER_MAXIMUM_AGE;
import static nextstep.member.domain.DiscountPolicy.Constants.TEENAGER_MINIMUM_AGE;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

public enum DiscountPolicy {


    CHILDREN(
        (age) -> CHILDREN_MINIMUM_AGE <= age && age < TEENAGER_MINIMUM_AGE,
        (fare) -> DEFAULT_DEDUCTED_AMOUNT + (int) ((fare - DEFAULT_DEDUCTED_AMOUNT) * (1 - CHILDREN_DISCOUNT_RATE))
    ),
    TEENAGER(
        (age) -> TEENAGER_MINIMUM_AGE <= age && age <= TEENAGER_MAXIMUM_AGE,
        (fare) -> DEFAULT_DEDUCTED_AMOUNT + (int) ((fare - DEFAULT_DEDUCTED_AMOUNT) * (1 - TEENAGER_DISCOUNT_RATE))
    ),
    ADULT(
        (age) -> TEENAGER_MAXIMUM_AGE < age,
        (fare) -> fare
    ),
    ADMIN(
        (age) -> true,
        (fare) -> fare
    ),
    ;

    private final IntPredicate ageLimit;
    private final IntFunction<Integer> policy;

    DiscountPolicy(IntPredicate ageLimit, IntFunction<Integer> policy) {
        this.ageLimit = ageLimit;
        this.policy = policy;
    }

    public int calculateDiscountFare(int fare) {
        return policy.apply(fare);
    }

    public static DiscountPolicy findDiscountPolicy(int age) {
        return Arrays.stream(values())
            .filter(it -> it.ageLimit.test(age))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException());
    }

    protected static class Constants {

        public static final int DEFAULT_DEDUCTED_AMOUNT = 350;
        public static final int CHILDREN_MINIMUM_AGE = 6;
        public static final int TEENAGER_MINIMUM_AGE = 13;
        public static final int TEENAGER_MAXIMUM_AGE = 18;
        public static final double TEENAGER_DISCOUNT_RATE = 0.2;
        public static final double CHILDREN_DISCOUNT_RATE = 0.5;
    }
}
