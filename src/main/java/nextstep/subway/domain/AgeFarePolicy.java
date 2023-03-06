package nextstep.subway.domain;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

@RequiredArgsConstructor
public enum AgeFarePolicy {
    쳥소년(
            age -> age >= 13 && age < 19,
            fare -> (int) ((fare - 350) * 0.8)),
    어린이(
            age -> age >= 6 && age < 13,
            fare -> (int) ((fare - 350) * 0.5)),
    NONE(age -> false, fare -> fare);

    private final IntPredicate matchPredicate;
    private final IntFunction<Integer> calcFareFunc;

    public static AgeFarePolicy of(int age) {
        return Arrays.stream(values())
                .filter(e -> e.matchPredicate.test(age))
                .findAny()
                .orElse(NONE);
    }

    public int calculateFare(int fare) {
        return calcFareFunc.apply(fare);
    }
}
