package nextstep.subway.domain;

import java.util.Arrays;

public enum Fare {
    STANDARD(0, 10, 0),
    OVER_10_KILOMETER(11, 50, 5),
    OVER_50_KILOMETER(51, Integer.MAX_VALUE, 8);

    private static final int BASIC_FARE = 1250;

    private final int minDistance;
    private final int maxDistance;
    private final int divideNumber;

    Fare(final int minDistance, final int maxDistance, final int divideNumber) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.divideNumber = divideNumber;
    }

    public static int calculateOverFare(final int distance) {
        if (distance <= STANDARD.maxDistance) {
            return BASIC_FARE;
        }

        final int overDistance = distance - STANDARD.maxDistance;
        final int divideNumber = getDivideNumber(distance);

        return BASIC_FARE + (int) ((Math.ceil((overDistance - 1) / divideNumber) + 1) * 100);
    }

    private static int getDivideNumber(final int distance) {
        return Arrays.stream(values())
            .filter(f -> isBetweenDistance(distance, f))
            .findFirst()
            .orElse(STANDARD)
            .divideNumber;
    }

    private static boolean isBetweenDistance(final int distance, final Fare fare) {
        return fare.minDistance <= distance && distance <= fare.maxDistance;
    }
}
