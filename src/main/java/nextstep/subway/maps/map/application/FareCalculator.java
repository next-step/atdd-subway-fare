package nextstep.subway.maps.map.application;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    public static final int BASIC_FARE = 1250;
    public static final int OVER_FARE_DISTANCE = 10;
    private static final int SUPER_OVER_FARE_DISTANCE = 50;

    public int calculate(int distance) {
        int overFare = 0;
        if (distance > SUPER_OVER_FARE_DISTANCE) {
            int overDistance = distance - SUPER_OVER_FARE_DISTANCE;
            overFare += calculateSuperOverFare(overDistance);
            distance -= overDistance;
        }
        if (distance > OVER_FARE_DISTANCE) {
            int overDistance = distance - OVER_FARE_DISTANCE;
            overFare += calculateOverFare(overDistance);
        }
        return BASIC_FARE + overFare;
    }

    private int calculateSuperOverFare(int overDistance) {
        return (int) ((Math.ceil((overDistance - 1) / 8) + 1) * 100);
    }

    private int calculateOverFare(int overDistance) {
        return (int) ((Math.ceil((overDistance - 1) / 5) + 1) * 100);
    }
}
