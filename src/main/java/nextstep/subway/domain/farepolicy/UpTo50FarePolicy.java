package nextstep.subway.domain.farepolicy;

public class UpTo50FarePolicy implements FarePolicy {
    private static final int EXCLUDED_DISTANCE = 10;
    private static final int MAX_FARE_DISTANCE = 40;
    private static final int UNIT_DISTANCE = 5;
    private static final int RATE_PER_UNIT = 100;

    @Override
    public int calculate(int distance) {
        if (distance <= EXCLUDED_DISTANCE) {
            return 0;
        }
        int unitSize = distanceForFare(distance) / UNIT_DISTANCE + 1;
        return unitSize * RATE_PER_UNIT;
    }

    private int distanceForFare(int distance) {
        if (distance < EXCLUDED_DISTANCE) {
            return 0;
        }
        return Math.min(distance - EXCLUDED_DISTANCE, MAX_FARE_DISTANCE);
    }
}
