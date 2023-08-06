package nextstep.subway.domain;

public class FareUtils {

    private static final int OVER_FARE_PER_INTERVAL = 100;

    private FareUtils() {
    }

    public static int calculateOverFare(int interval, int overDistance) {

        if (overDistance == 0) {
            return 0;
        }

        return (int) ((Math.ceil((overDistance - 1) / interval) + 1) * OVER_FARE_PER_INTERVAL);
    }
}
