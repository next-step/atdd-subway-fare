package nextstep.subway.domain;

public class FareUtils {

    private FareUtils() {
    }

    public static int calculateOverFare(int overDistance, int interval, int overFarePerInterval) {

        if (overDistance <= 0) {
            return 0;
        }

        return (int) ((Math.ceil((overDistance - 1) / interval) + 1) * overFarePerInterval);
    }
}
