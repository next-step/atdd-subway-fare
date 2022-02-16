package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum DistanceFarePolicy {
    BASE_SECTION(value -> 1_250),
    FIRST_OVER_SECTION(distance -> {
        if (distance < 10) {
            return 0;
        }
        int excessDistance = Math.min(distance, 50) - 10;
        return calculateOverFare(excessDistance, 5, 100);
    }),
    SECOND_OVER_SECTION(distance -> {
        if (distance < 50) {
            return 0;
        }
        int excessDistance = distance - 50;
        return calculateOverFare(excessDistance, 8, 100);
    }),
    ;

    private final Function<Integer, Integer> distanceToOverFare;

    DistanceFarePolicy(final Function<Integer, Integer> distanceToOverFare) {
        this.distanceToOverFare = distanceToOverFare;
    }

    public static int calculate(int distance) {
        return Arrays.stream(DistanceFarePolicy.values()).mapToInt(it -> it.distanceToOverFare.apply(distance)).sum();
    }

    private static int calculateOverFare(int distance, int unitDistance, int extraFare) {
        return (int) ((Math.ceil((distance - 1) / unitDistance) + 1) * extraFare);
    }
}