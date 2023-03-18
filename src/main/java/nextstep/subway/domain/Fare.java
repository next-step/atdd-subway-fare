package nextstep.subway.domain;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final int DEFAULT_DISTANCE = 10;

    public static int calculateAmount(final int distance) {
        if (distance <= DEFAULT_DISTANCE) {
            return BASIC_FARE;
        }
        else if (distance > DEFAULT_DISTANCE && distance <= 50) {
            return BASIC_FARE + calculateOverFare(distance - DEFAULT_DISTANCE, DistanceRange.OVER_10_AND_UNDER_50);
        }
        else {
            return BASIC_FARE + calculateOverFare(40, DistanceRange.OVER_10_AND_UNDER_50) + calculateOverFare(distance - 50, DistanceRange.OVER_50);
        }
    }

    private static int calculateOverFare(final int distance, final DistanceRange distanceRange) {
        return (int) ((Math.ceil((distance - 1) / distanceRange.getIntervalDistance()) + 1) * distanceRange.getAmountPerDistance());
    }
}
