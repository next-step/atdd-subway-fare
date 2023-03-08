package nextstep.subway.domain;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

@RequiredArgsConstructor
public enum AgeFarePolicy {
    무료(age -> age < 6, fare -> 0),
    쳥소년(age -> age >= 13 && age < 19, fare -> calculateFareByWeight(fare, 0.8)),
    어린이(age -> age >= 6 && age < 13, fare -> calculateFareByWeight(fare, 0.5)),
    성인(age -> false, fare -> fare);

    private static final int FIXED_DISCOUNT = 350;
    private final IntPredicate matchPredicate;
    private final IntFunction<Integer> calculateFareFunc;

    public static AgeFarePolicy of(int age) {
        return Arrays.stream(values())
                .filter(e -> e.matchPredicate.test(age))
                .findAny()
                .orElse(성인);
    }

    public int calculateFare(int fare) {
        return calculateFareFunc.apply(fare);
    }

    private static int calculateFareByWeight(int fare, double weight) {
        return (int) ((fare - FIXED_DISCOUNT) * weight);
    }
}
