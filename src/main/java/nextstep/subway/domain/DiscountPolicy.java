package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum DiscountPolicy {
    BABY(0, 6, fare -> 0),
    CHILDREN(6, 13, fare -> calculateDiscountFare(fare, 50)),
    YOUTH(13, 19, fare -> calculateDiscountFare(fare, 20)),
    ADULT(19, 1000, fare -> fare),
    ;

    private final int minAge;
    private final int maxAge;
    private final Function<Integer, Integer> fareToDiscountFare;

    DiscountPolicy(final int minAge, final int maxAge, final Function<Integer, Integer> fareToDiscountFare) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.fareToDiscountFare = fareToDiscountFare;
    }

    public static int discount(int age, int fare) {
        return getPolicyFromAge(age).fareToDiscountFare.apply(fare);
    }

    public static DiscountPolicy getPolicyFromAge(int age) {
        return Arrays.stream(DiscountPolicy.values())
                .filter(it -> it.minAge <= age && it.maxAge > age)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private static int calculateDiscountFare(final int fare, final int discountRate) {
        if (fare > 350) {
            return (fare - 350) * (100 - discountRate) / 100 + 350;
        }

        return fare;
    }
}