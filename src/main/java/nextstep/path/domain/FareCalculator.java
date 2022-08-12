package nextstep.path.domain;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {
    private static final int BASE_FARE_DISTANCE = 10;
    private static final int LONG_DISTANCE = 50;

    private static final int BASE_FARE = 1250;
    private static final int BONUS_FARE = 100;

    private static final int SHORT_DISTANCE_BONUS_INTERVAL = 5;
    private static final int LONG_DISTANCE_BONUS_INTERVAL = 8;

    public int calculateFare(int distance) {
        return BASE_FARE + calculateOverFare(distance);
    }

    private int calculateOverFare(int distance) {
        if (distance <= BASE_FARE_DISTANCE) {
            return 0;
        }

        if (distance <= LONG_DISTANCE) {
            int overDistance = distance - BASE_FARE_DISTANCE;
            int overTimes = (int) Math.ceil((overDistance + 1d) / SHORT_DISTANCE_BONUS_INTERVAL);
            return overTimes * BONUS_FARE;
        }

        int overDistance = distance - LONG_DISTANCE;
        int overTimes = (int) Math.ceil((overDistance + 1d) / LONG_DISTANCE_BONUS_INTERVAL);
        return maxShortDistanceOverFare() + overTimes * BONUS_FARE;

    }

    private int maxShortDistanceOverFare() {
        int overDistance = LONG_DISTANCE - (BASE_FARE_DISTANCE  + 1);
        int overTimes = (int) Math.ceil((overDistance + 1d) / SHORT_DISTANCE_BONUS_INTERVAL);
        return overTimes * BONUS_FARE;
    }
}
