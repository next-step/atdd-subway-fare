package nextstep.subway.domain.fare;

import nextstep.member.domain.Member;

import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.stream.Stream;

public enum DiscountPolicy {
    CHILD(age -> age >= 6 && age < 13,
            fare -> fare - (int) ((fare - 350) * 0.5)),
    YOUTH(age -> age >= 13 && age < 19,
            fare -> fare - (int) ((fare - 350) * 0.2)),
    GENERAL(age -> age >= 19, fare -> fare);

    private final IntPredicate supported;
    private final IntFunction<Integer> discount;

    DiscountPolicy(IntPredicate supported, IntFunction<Integer> discount) {
        this.supported = supported;
        this.discount = discount;
    }

    public static DiscountPolicy of(Member member) {
        return Stream.of(values())
                .filter(s -> s.supported(member.getAge()))
                .findFirst()
                .orElse(GENERAL);
    }

    public boolean supported(int age) {
        return supported.test(age);
    }

    public int apply(int fare) {
        return discount.apply(fare);
    }
}
