package nextstep.subway.applicaion;

public class FareCalculator {

    private static final int BASIC_FARE = 1_250;
    private static final int ADDITIONAL_FARE_PER_DISTANCE = 100;
    private static final int BASIC_DISTANCE = 10;
    private static final int LONG_DISTANCE = 50;
    private static final int UNIT_BELOW_LONG_DISTANCE = 5;
    private static final int UNIT_OVER_LONG_DISTANCE = 8;

    private FareCalculator() {
    }

    public static int calculate(int distance) {
        if (distance <= BASIC_DISTANCE) {
            return BASIC_FARE;
        }
        if (distance <= LONG_DISTANCE) {
            return BASIC_FARE + calculateOverFare(distance - BASIC_DISTANCE, UNIT_BELOW_LONG_DISTANCE);
        }
        return BASIC_FARE + calculateOverFare(distance - BASIC_DISTANCE, UNIT_OVER_LONG_DISTANCE);
    }

    private static int calculateOverFare(int distance, int unit) {
        return (int) ((Math.ceil((distance - 1) / unit) + 1) * ADDITIONAL_FARE_PER_DISTANCE);
    }
}
