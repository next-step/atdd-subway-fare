package nextstep.subway.domain.discount;

import nextstep.member.domain.Member;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

import static nextstep.subway.domain.discount.DiscountPolicyByAgeCalculator.AgeDiscountPolicy.*;


public enum DiscountPolicyByAgeCalculator {
    TODDLER_POLICY(TODDLER, fare -> 0L),
    CHILDREN_POLICY(CHILDREN, fare -> (long) ((fare - 350) * 0.5 + 350)),
    TEENAGER_POLICY(TEENAGER, fare -> (long) ((fare - 350) * 0.8 + 350)),
    ADULT_POLICY(ADULT, fare -> fare);

    private AgeDiscountPolicy discountPolicy;

    private Function<Long, Long> mapper;

    DiscountPolicyByAgeCalculator(AgeDiscountPolicy discountPolicy, Function<Long, Long> mapper) {
        this.discountPolicy = discountPolicy;
        this.mapper = mapper;
    }

    public long discountFare(Member member, long fare) {
        return applyToDiscountFare(member, fare);
    }

    public static long applyToDiscountFare(Member member, long fare) {
        AgeDiscountPolicy ageDiscountPolicy = get(member);
        return Arrays.stream(DiscountPolicyByAgeCalculator.values())
                .filter(d -> d.discountPolicy == ageDiscountPolicy)
                .map(d -> d.mapper.apply(fare))
                .findFirst()
                .orElse(fare);
    }

    public enum AgeDiscountPolicy {
        TODDLER(age -> age <6),
        CHILDREN(age -> (age >= 6 && age <13)),
        TEENAGER(age -> (age >= 13 && age <19)),
        ADULT(age -> age >= 19);

        private Predicate<Integer> condition;

        AgeDiscountPolicy(Predicate<Integer> condition) {
            this.condition = condition;
        }

        public static AgeDiscountPolicy get(Member member) {
            return Arrays.stream(values())
                    .filter(p -> p.condition.test(member.getAge()))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }

}
