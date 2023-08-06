package nextstep.subway.utils;

public class CalculateFareUtils {

    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_FARE_DISTANCE = 10;

    private CalculateFareUtils() {
        throw new RuntimeException("is Util Class");
    }

    public static int getFare(int distance) {

        if (distance == 0) {
            return 0;
        }

        if (distance > DEFAULT_FARE_DISTANCE && distance <= 50) {
            return DEFAULT_FARE + calculateOverFareUnder50(distance - DEFAULT_FARE_DISTANCE);
        }

        if (distance > 50) {
            return DEFAULT_FARE + calculateOverFareOver50(distance - DEFAULT_FARE_DISTANCE);
        }

        return DEFAULT_FARE;
    }

    private static int calculateOverFareUnder50(int overDistance) {

        return (int) ((Math.ceil((overDistance - 1) / 5) + 1) * 100);
    }

    private static int calculateOverFareOver50(int overDistance) {

        int chargeDistance = overDistance - 40;
        return calculateOverFareUnder50(40) + (int) ((Math.ceil((chargeDistance - 1) / 8) + 1) * 100);
    }
}
