package nextstep.subway.domain.fare;

import java.util.function.IntFunction;
import java.util.function.IntPredicate;

public enum FarePolicy {
    NONE(distance -> distance <= 10, distance -> 0),
    OVER_10_KM(distance -> distance > 10,
            distance -> {
                if (distance > 50) {
                    distance = 50;
                }
                return calculate(distance - 10, 5);
            }),
    OVER_50_KM(distance -> distance > 50, distance -> calculate(distance - 50, 8));

    private static final int DEFAULT_PER_FARE = 100;

    private final IntPredicate predicate;
    private final IntFunction<Integer> function;

    FarePolicy(IntPredicate predicate, IntFunction<Integer> function) {
        this.predicate = predicate;
        this.function = function;
    }

    private static int calculate(int distance, int perDistance) {
        return (int) ((Math.ceil((distance - 1) / perDistance) + 1) * DEFAULT_PER_FARE);
    }

    public boolean supported(int distance) {
        return predicate.test(distance);
    }

    public int additionalFare(int distance) {
        return function.apply(distance);
    }
}
