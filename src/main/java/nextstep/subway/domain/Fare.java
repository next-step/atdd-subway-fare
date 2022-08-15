package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public enum Fare {
    BASIC(0, 10, distance -> 1250),
    TEN_TO_FIFTY_KILO(11, 50, distance -> Math.min (800, (int) ((Math.ceil(distance - 11) / 5) + 1) * 100)),
    OVER_FIFTY_KILO(51, Integer.MAX_VALUE, distance -> (int) ((Math.ceil(distance - 51) / 8) + 1) * 100);

    private final int minDistance;
    private final int maxDistance;
    private final ToIntFunction<Integer> operation;

    Fare(int minDistance, int maxDistance, ToIntFunction<Integer> operation) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.operation = operation;
    }

    public static int getFare(int distance) {
        validateDistance(distance);
        return calculateFare(distance);
    }

    private static void validateDistance(int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException();
        }
    }

    private static int calculateFare(int distance) {
        return Arrays.stream(values())
                .filter(fare -> isUpperThanMinDistance(distance, fare))
                .mapToInt(fare -> fare.operation.applyAsInt(distance))
                .sum();
    }

    private static boolean isUpperThanMinDistance(int distance, Fare fare) {
        return fare.minDistance <= distance;
    }

}
