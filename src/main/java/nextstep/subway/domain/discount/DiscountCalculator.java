package nextstep.subway.domain.discount;

import nextstep.member.domain.Member;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

import static nextstep.subway.domain.discount.DiscountCalculator.DiscountPolicy.*;

public enum DiscountCalculator {
    TODDLER_POLICY(TODDLER, fare -> 0L),
    CHILDREN_POLICY(CHILDREN, fare -> (long) ((fare - 350) * 0.5 + 350)),
    TEENAGER_POLICY(TEENAGER, fare -> (long) ((fare - 350) * 0.8 + 350)),
    ADULT_POLICY(ADULT, fare -> fare);

    private DiscountPolicy discountPolicy;

    private Function<Long, Long> mapper;

    DiscountCalculator(DiscountPolicy discountPolicy, Function<Long, Long> mapper) {
        this.discountPolicy = discountPolicy;
        this.mapper = mapper;
    }

    public static long applyToDiscountFare(DiscountPolicy discountPolicy, long fare) {
        return Arrays.stream(DiscountCalculator.values())
                .filter(d -> d.discountPolicy == discountPolicy)
                .map(d -> d.mapper.apply(fare))
                .findFirst()
                .orElse(fare);
    }

    public enum DiscountPolicy {
        TODDLER(age -> age <6),
        CHILDREN(age -> (age >= 6 && age <13)),
        TEENAGER(age -> (age >= 13 && age <19)),
        ADULT(age -> age >= 19);

        private Predicate<Integer> condition;

        DiscountPolicy(Predicate<Integer> condition) {
            this.condition = condition;
        }

        public static DiscountPolicy get(Member member) {
            return Arrays.stream(values())
                    .filter(p -> p.condition.test(member.getAge()))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }

    }

}
