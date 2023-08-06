package nextstep.subway.domain;

public class FareCalculator {

    private static final int BASE_FARE = 1250;
    private static final int OVER_FARE = 100;
    private static final int OVER_FARE_INTERVAL_UNTIL_50KM = 5;
    private static final int OVER_FARE_INTERVAL_AFTER_50KM = 8;

    private int distance;

    public FareCalculator(int distance) {
        this.distance = distance;
    }

    public int fare() {
        return 1250;
    }
}
