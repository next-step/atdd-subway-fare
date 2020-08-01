package nextstep.subway.maps.map.application;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    public static final int BASIC = 1250;
    public static final int OVERFARE_DISTANCE = 10;

    public int calculate(int distance) {
        if (distance > OVERFARE_DISTANCE) {
            return BASIC + calculateOverFare(distance - OVERFARE_DISTANCE);
        }
        return BASIC;
    }

    private int calculateOverFare(int distance) {
        return (int)((Math.ceil(distance - 1) / 5) + 1) * 100;
    }
}
