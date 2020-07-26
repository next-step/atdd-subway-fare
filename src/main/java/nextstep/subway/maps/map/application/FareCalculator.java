package nextstep.subway.maps.map.application;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    public static final int BASIC_FARE = 1250;

    public int calculate(int distance) {
        return BASIC_FARE;
    }
}
