package nextstep.subway.util.fare;

public class DistanceFarePolicy {
    private static final int DEFAULT_FARE = 1250;
    private static final int FIFTY_KILOMETER = 50;
    private static final int TEN_KILOMETER = 10;

    public static int calculate(int distance) {
        int fare = DEFAULT_FARE;

        if (distance > TEN_KILOMETER) {
            fare += calculateOverTen(distance);
        }

        if (distance > FIFTY_KILOMETER) {
            fare += calculateOverFifty(distance);
        }

        return fare;
    }

    private static int calculateOverTen(int distance) {
        int overDistance = Math.min(FIFTY_KILOMETER - TEN_KILOMETER, distance - TEN_KILOMETER);
        return (int) ((Math.ceil((overDistance - 1) / 5) + 1) * 100);
    }

    private static int calculateOverFifty(int distance) {
        return (int) ((Math.ceil((distance - FIFTY_KILOMETER - 1) / 8) + 1) * 100);
    }
}
