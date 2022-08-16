package nextstep.subway.domain;

public class SubwayFare {
    private static final int BASE_FARE_DISTANCE = 10;
    private static final int BASE_FARE = 1250;

    private static final int LONG_FARE_DISTANCE = 50;


    public int calculateFare(int distance) {
        int overDistance = distance - BASE_FARE_DISTANCE;
        int totalFare = BASE_FARE;

        if (distance > LONG_FARE_DISTANCE) {
            overDistance = LONG_FARE_DISTANCE - BASE_FARE_DISTANCE;
            totalFare += calculateOverFare(distance - LONG_FARE_DISTANCE, 8);
        }

        if (overDistance > 0) {
            totalFare += calculateOverFare(overDistance, 5);
        }

        return totalFare;
    }

    private int calculateOverFare(int overDistance, int fareDistance) {
        return (int) ((Math.ceil((overDistance - 1) / fareDistance) + 1) * 100);
    }
}
