package nextstep.subway.maps.map.application;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    public static final int BASIC = 1250;

    public int calculate(int distance) {
        return BASIC;
    }
}
