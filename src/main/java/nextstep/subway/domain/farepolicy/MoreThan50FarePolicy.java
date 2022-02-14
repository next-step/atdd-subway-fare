package nextstep.subway.domain.farepolicy;

public class MoreThan50FarePolicy implements FarePolicy {
    private static final int EXCLUDED_DISTANCE = 50;
    private static final int MAX_FARE_DISTANCE = Integer.MAX_VALUE;
    private static final int UNIT_DISTANCE = 8;
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
