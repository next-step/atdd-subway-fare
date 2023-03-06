package nextstep.subway.domain;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

import static nextstep.subway.domain.DistacneFarePolicy.Constants.*;

@RequiredArgsConstructor
public enum DistacneFarePolicy {
    DEFAULT(
            distance -> distance >= MIN_DISTANCE && distance <= DEFAULT_DISTANCE,
            distance -> DEFAULT_FARE),
    SECTION1(
            distance -> distance > DEFAULT_DISTANCE && distance <= SECTION1_DISTANCE,
            distance -> DEFAULT_FARE + calculateOverFare(distance - DEFAULT_DISTANCE, 5)),
    SECTION2(
            distance -> distance > SECTION1_DISTANCE,
            distance -> SECTION1.calculate(SECTION1_DISTANCE) + calculateOverFare(distance - SECTION1_DISTANCE, 8));

    private final IntPredicate matchPredicate;
    private final IntFunction<Integer> calculateFunc;

    public static DistacneFarePolicy of(int distance) {
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

    static class Constants {
        public static final int DEFAULT_DISTANCE = 10;
        public static final int MIN_DISTANCE = 0;
        public static final int DEFAULT_FARE = 1250;
        public static final int SECTION1_DISTANCE = 50;
    }
}
