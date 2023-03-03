package nextstep.subway.domain;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

@RequiredArgsConstructor
public enum Fare {
    DEFAULT(distance -> distance <= 10 && distance >= 0, distance -> 1250),
    SECTION1(distance -> distance <= 50 + 10 && distance > 10 + 10, distance -> 1250 + calculateOverFare(distance - 10, 5)),
    SECTION2(distance -> distance > 50 + 10, distance -> 1250 + 1000 + calculateOverFare(distance - 60, 8));

    private final IntPredicate matchPredicate;
    private final IntFunction<Integer> calculateFunc;

    public static Fare of(int distance) {
        return Arrays.stream(values())
                .filter(a -> a.matchPredicate.test(distance))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public int calculate(int distance) {
        return calculateFunc.apply(distance);
    }

    private static int calculateOverFare(int distance, int wight) {
        return (int) ((Math.ceil((distance - 1) / wight) + 1) * 100);
    }
}
