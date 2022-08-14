package nextstep.subway.domain;

import java.util.Arrays;

public enum Fare {
    BASIC(0, 10, 0),
    TEN_TO_FIFTY_KILO(11, 50, 5),
    OVER_FIFTY_KILO(51, Integer.MAX_VALUE, 8);

    private static final int BASIC_FARE = 1250;

    private final int minDistance;
    private final int maxDistance;
    private final int divideNumber;

    Fare(int minDistance, int maxDistance, int divideNumber) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.divideNumber = divideNumber;
    }

    public static int getFare(int distance) {
        if (distance <= BASIC.maxDistance) {
            return BASIC_FARE;
        }
        return BASIC_FARE + getAdditionalFare(distance);
    }

    private static int getAdditionalFare(int distance) {
        int additionalDistance = distance - BASIC.maxDistance;
        int divideNumber = getDivideNumber(distance);

        return (int) ((Math.ceil((additionalDistance - 1) / divideNumber) + 1) * 100);
    }

    private static int getDivideNumber(int distance) {
        return Arrays.stream(values())
                .filter(fare -> isBetweenDistance(distance, fare))
                .findFirst()
                .orElse(BASIC)
                .divideNumber;
    }

    private static boolean isBetweenDistance(int distance, Fare fare) {
        return fare.minDistance <= distance
                && distance <= fare.maxDistance;
    }

}
