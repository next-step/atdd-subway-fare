package nextstep.subway.domain;

import java.util.function.Function;
import java.util.function.Predicate;

public enum FarePolicy {
    NONE(distance -> distance <= 10, distance -> 0),
    OVER_10_KM(distance -> distance > 10,
            distance -> {
                if (distance > 50) {
                    distance = 50;
                }
                return calculate(distance - 10, 5, 100);
            }),
    OVER_50_KM(distance -> distance > 50, distance -> calculate(distance - 50, 8, 100));

    private final Predicate<Integer> predicate;
    private final Function<Integer, Integer> function;

    FarePolicy(Predicate<Integer> predicate, Function<Integer, Integer> function) {
        this.predicate = predicate;
        this.function = function;
    }

    private static int calculate(int distance, int perDistance, int perFare) {
        return (int) ((Math.ceil((distance - 1) / perDistance) + 1) * perFare);
    }

    public boolean supported(int distance) {
        return predicate.test(distance);
    }

    public int additionalFare(int distance) {
        return function.apply(distance);
    }
}
