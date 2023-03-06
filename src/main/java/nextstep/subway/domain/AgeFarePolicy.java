package nextstep.subway.domain;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

@RequiredArgsConstructor
public enum AgeFarePolicy {
    쳥소년(age -> age >= 13 && age < 19, 0.8),
    어린이(age -> age >= 6 && age < 13,0.5),
    NONE(age -> false, 0);

    private static final int FIXED_DISCOUNT = 350;
    private final IntPredicate matchPredicate;
    private final double weight;

    public static AgeFarePolicy of(int age) {
        return Arrays.stream(values())
                .filter(e -> e.matchPredicate.test(age))
                .findAny()
                .orElse(NONE);
    }

    public int calculateFare(int fare) {
        return calculateFare(fare, weight);
    }

    public int calculateFare(int fare, double weight) {
        if (weight == 0) {
            return fare;
        }

        return  (int) ((fare - FIXED_DISCOUNT) * weight);
    }
}
