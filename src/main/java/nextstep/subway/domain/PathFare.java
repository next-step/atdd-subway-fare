package nextstep.subway.domain;

import java.math.BigDecimal;
import java.util.Arrays;

public enum PathFare {
    BASIC_DISTANCE_FARE(0, 10, 0, 1250),
    SHORT_DISTANCE_FARE(11, 50, 5, 100),
    LONG_DISTANCE_FARE(51, Integer.MAX_VALUE, 8,  100);

    private int standardDistance;
    private int maxDistance;
    private int distanceUnit;
    private int fare;

    PathFare(int standardDistance, int maxDistance, int distanceUnit, int fare) {
        this.standardDistance = standardDistance;
        this.maxDistance = maxDistance;
        this.distanceUnit = distanceUnit;
        this.fare = fare;
    }

    public static BigDecimal extractFare(int distance) {
        int sumFare = Arrays.stream(PathFare.values())
                .mapToInt(pathFare -> pathFare.calculateOverFare(distance))
                .sum();

        return BigDecimal.valueOf(sumFare);
    }

    private int calculateOverFare(int distance) {
        if (this == PathFare.BASIC_DISTANCE_FARE) {
            return fare;
        }

        if (distance < standardDistance) {
            return 0;
        }

        int overDistance = Integer.min(distance, maxDistance);
        return (int) ((Math.ceil((overDistance - standardDistance) / distanceUnit) + 1) * fare);
    }
}
