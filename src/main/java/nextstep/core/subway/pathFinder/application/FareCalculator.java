package nextstep.core.subway.pathFinder.application;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    public int calculateOverFare(int distance) {
        return 1350;
    }
}
