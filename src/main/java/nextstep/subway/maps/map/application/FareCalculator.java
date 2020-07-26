package nextstep.subway.maps.map.application;

import org.springframework.lang.Nullable;

public class FareCalculator {
    public static final int DEFAULT_FARE = 1250;
    private static final int OVER_DISTANCE_FARE_UNIT = 100;

    public int calculate(int distance) {
        int fare = DEFAULT_FARE;
        fare += calculateOverFare(distance, 10, 50, 5);
        fare += calculateOverFare(distance, 50, null, 8);

        return fare;
    }

    private int calculateOverFare(Integer distance, Integer min, @Nullable Integer max, Integer unitDistance) {
        if (distance <= min) {
            return 0;
        }

        if (max != null && distance > max) {
            return calculateOverFare(max - min, unitDistance);
        }

        int overDistance = distance - min;
        return calculateOverFare(overDistance, unitDistance);
    }

    private int calculateOverFare(int overDistance, int unitDistance) {
        return (int) ((Math.ceil((overDistance - 1) / unitDistance) + 1) * OVER_DISTANCE_FARE_UNIT);
    }
}
