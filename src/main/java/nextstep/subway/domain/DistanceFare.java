package nextstep.subway.domain;

import java.math.BigDecimal;
import java.util.Arrays;

public enum DistanceFare {
    BASIC_DISTANCE_FARE(0, 10, 0, 1250),
    SHORT_DISTANCE_FARE(11, 50, 5, 100),
    LONG_DISTANCE_FARE(51, Integer.MAX_VALUE, 8,  100);

    private final int standardDistance;
    private final int maxDistance;
    private final int distanceUnit;
    private final int fare;

    DistanceFare(int standardDistance, int maxDistance, int distanceUnit, int fare) {
        this.standardDistance = standardDistance;
        this.maxDistance = maxDistance;
        this.distanceUnit = distanceUnit;
        this.fare = fare;
    }

    public static BigDecimal extractFare(int distance) {
        int sumFare = Arrays.stream(DistanceFare.values())
                .mapToInt(pathFare -> pathFare.calculateOverFare(distance))
                .sum();

        return BigDecimal.valueOf(sumFare);
    }

    private int calculateOverFare(int distance) {
        if (this == DistanceFare.BASIC_DISTANCE_FARE) {
            return fare;
        }

        if (distance < standardDistance) {
            return 0;
        }

        int overDistance = Integer.min(distance, maxDistance);
        return (int) ((Math.ceil((overDistance - standardDistance) / distanceUnit) + 1) * fare);
    }
}
