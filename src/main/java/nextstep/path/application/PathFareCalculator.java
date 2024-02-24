package nextstep.path.application;

public class PathFareCalculator {

    public static final long DEFAULT_FARE = 1250L;
    public static final long EXTRA_FARE_BASE = 100L;
    public static final int FIRST_STANDARD_DISTANCE = 10;
    public static final int FIRST_FARE_INTERVAL = 5;
    public static final int SECOND_STANDARD_DISTANCE = 50;
    public static final int SECOND_FARE_INTERVAL = 8;

    public long calculate(final int distance) {
        if (distance <= FIRST_STANDARD_DISTANCE) {
            return DEFAULT_FARE;
        }

        int firsDistanceExtraFare = 0;
        if (distance <= SECOND_STANDARD_DISTANCE) {
            firsDistanceExtraFare += calculateOverFare(distance - FIRST_STANDARD_DISTANCE, FIRST_FARE_INTERVAL);
        }

        if (SECOND_STANDARD_DISTANCE < distance) {
            firsDistanceExtraFare += calculateOverFare(SECOND_STANDARD_DISTANCE - FIRST_STANDARD_DISTANCE, FIRST_FARE_INTERVAL);
            firsDistanceExtraFare += calculateOverFare(distance - SECOND_STANDARD_DISTANCE, SECOND_FARE_INTERVAL);
        }

        return DEFAULT_FARE + firsDistanceExtraFare;
    }

    private int calculateOverFare(final int distance, final int intervalDistance) {
        return (int) ((Math.floor((double) (distance - 1) / intervalDistance) + 1) * EXTRA_FARE_BASE);
    }
}
