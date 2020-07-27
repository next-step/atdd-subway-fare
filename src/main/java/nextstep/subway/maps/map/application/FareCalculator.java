package nextstep.subway.maps.map.application;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    public static final int BASIC_FARE = 1250;
    public static final int OVER_FARE_DISTANCE = 10;

    public int calculate(int distance) {
        if (distance > OVER_FARE_DISTANCE) {
            return BASIC_FARE + calculateOverFare(distance - OVER_FARE_DISTANCE);
        }
        return BASIC_FARE;
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
