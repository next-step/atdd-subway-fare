package nextstep.core.subway.pathFinder.application;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    public static final int BASE_FARE = 1250;

    public static final int STANDARD_DISTANCE = 10;
    public static final int STANDARD_RATE = 5;

    public static final int LONG_DISTANCE = 50;
    public static final int LONG_RATE = 8;

    public int calculateFare(int distance) {
        int fare = BASE_FARE;

        if (distance > STANDARD_DISTANCE && distance <= LONG_DISTANCE) {
            fare += calculateOverFare(distance - STANDARD_DISTANCE, STANDARD_RATE);
        }
        if (distance > LONG_DISTANCE) {
            fare += calculateOverFare(LONG_DISTANCE - STANDARD_DISTANCE, STANDARD_RATE);
            fare += calculateOverFare(distance - LONG_DISTANCE, LONG_RATE);
        }
        return fare;
    }

    private int calculateOverFare(int distance, int rate) {
        return (int) (((double) ((distance - 1) / rate) + 1) * 100);
    }
}
