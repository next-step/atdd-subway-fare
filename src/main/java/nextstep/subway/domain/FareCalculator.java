package nextstep.subway.domain;

import static nextstep.subway.domain.FareUtils.calculateOverFare;

public class FareCalculator {

    private static final int BASE_FARE = 1250;
    private static final int OVER_FARE = 100;
    private static final int OVER_FARE_INTERVAL_UNTIL_50KM = 5;
    private static final int OVER_FARE_INTERVAL_AFTER_50KM = 8;


    private int distanceBasic;
    private int distanceOver10km;
    private int distanceOver50km;

    public FareCalculator(int distance) {
        this.distanceBasic = Math.min(distance, 10);
        this.distanceOver10km = Math.min(Math.max(distance - 10, 0), 40);
        this.distanceOver50km = Math.max(distance - 50, 0);
    }

    public int fare() {
        return BASE_FARE + calculateOverFare(this.distanceOver10km, OVER_FARE_INTERVAL_UNTIL_50KM, OVER_FARE)
                + calculateOverFare(this.distanceOver50km, OVER_FARE_INTERVAL_AFTER_50KM, OVER_FARE);
    }
}
