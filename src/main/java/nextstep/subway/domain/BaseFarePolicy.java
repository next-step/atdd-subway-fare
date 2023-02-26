package nextstep.subway.domain;

public class BaseFarePolicy implements FarePolicy {
    public static final int BASE_FARE = 1250;
    private static final int BASE_FARE_DISTANCE = 10;
    private static final int FIFTY_KM = 50;
    private static final int BASE_OVER_FARE = 100;
    private static final int OVER_10KM_BASE_FARE_DISTANCE = 5;
    private static final int OVER_50KM_BASE_FARE_DISTANCE = 8;

    @Override
    public int calculate(int distance) {
        int fare = BASE_FARE;
        if (distance > BASE_FARE_DISTANCE) {
            fare += calculateOverFare(distance - BASE_FARE_DISTANCE);
        }
        return fare;
    }

    private int calculateOverFare(int distance) {
        int overFare = calculateOverFare(OVER_10KM_BASE_FARE_DISTANCE, distance);
        if (distance > FIFTY_KM) {
            overFare += calculateOverFare(OVER_50KM_BASE_FARE_DISTANCE, distance - FIFTY_KM);
        }
        return overFare;
    }

    private int calculateOverFare(int baseFareDistance, int distance) {
        return (int) ((Math.ceil((distance - 1) / baseFareDistance) + 1) * BASE_OVER_FARE);
    }
}
