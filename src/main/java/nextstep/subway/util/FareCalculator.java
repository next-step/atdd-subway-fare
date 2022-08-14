package nextstep.subway.util;

public class FareCalculator {
    private static final int DEFAULT_FARE = 1250;
    private static final int TEN_KILO_METER = 10;
    private static final int FIFTY_KILO_METER = 50;

    public static int calculate(int distance) {
        return DEFAULT_FARE + calculateOverFare(distance);
    }

    private static int calculateOverFare(int distance) {
        if (distance <= TEN_KILO_METER) {
            return 0;
        }

        if (distance <= FIFTY_KILO_METER) {
            return (int) ((Math.ceil((distance - TEN_KILO_METER - 1) / 5) + 1) * 100);
        }

        return (int) ((Math.ceil((FIFTY_KILO_METER - TEN_KILO_METER - 1) / 5) + 1) * 100)
                + (int) ((Math.ceil((distance - FIFTY_KILO_METER - 1) / 8) + 1) * 100);
    }
}
