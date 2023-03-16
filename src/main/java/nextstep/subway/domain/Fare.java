package nextstep.subway.domain;

public class Fare {
    private static final int DEFAULT_FARE_DISTANCE = 10;
    private static final int DEFAULT_FARE = 1250;

    private Fare() {
    }

    public static int calculate(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("요금 계산시 거리는 1 이상 이어야 합니다.");
        }
        int fare = DEFAULT_FARE;
        if (distance <= DEFAULT_FARE_DISTANCE) {
            return fare;
        }
        if (distance <= 50) {
            fare += calculateOverFare10To50(distance - DEFAULT_FARE_DISTANCE);
            return fare;
        }
        fare += calculateOverFare50Over(distance - DEFAULT_FARE_DISTANCE);
        return fare;
    }

    private static int calculateOverFare10To50(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private static int calculateOverFare50Over(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

}
