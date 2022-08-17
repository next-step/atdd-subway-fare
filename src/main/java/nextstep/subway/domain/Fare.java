package nextstep.subway.domain;


import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.function.ToIntFunction;


@AllArgsConstructor
public enum Fare {
    BASIC(0, 10, distance -> 1250),
    TEN_TO_FIFTY(11, 50, distance -> (int) (Math.ceil((distance - 11) / 5.) + 1) * 100),
    OVER_TO_FIFTY(51, Integer.MAX_VALUE, distance -> (int) (Math.ceil((distance - 51.) / 8) + 1) * 100);

    private final int minDistance;
    private final int maxDistance;
    private final ToIntFunction<Integer> operate;

    public static int getFare(int distance) {
        return calculate(distance);
    }

    private static int calculate(int distance) {
      return Arrays.stream(values())
                   .filter(fare -> isMoreThanMinDistance(distance, fare))
                   .mapToInt(fare -> fare.operate.applyAsInt(distance))
                   .sum();
    }

    private static boolean isMoreThanMinDistance(int distance, Fare fare) {
        return fare.minDistance <= distance;
    }

}
