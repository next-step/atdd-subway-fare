package nextstep.member.domain;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

public enum DiscountPolicy {

    CHILDREN(
        (age) -> age >= 6 && age < 13,
        (fare) -> 350 + (int) ((fare - 350) * 0.5)
    ),
    TEENAGER(
        (age) -> age >= 13 && age < 19,
        (fare) -> 350 + (int) ((fare - 350) * 0.8)
    ),
    ADULT(
        (age) -> age >= 19,
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
}
