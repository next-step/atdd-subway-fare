package nextstep.subway.domain;

public class FareCalculator {

    private static final int BASE_FARE = 1250;
    private static final int OVER_FARE = 100;
    private static final int OVER_FARE_INTERVAL_UNTIL_50KM = 5;
    private static final int OVER_FARE_INTERVAL_AFTER_50KM = 8;

    private static final int FIRST_OVER_FARE_SECTION_START = 10;
    private static final int FIRST_OVER_FARE_SECTION_MAX_DISTANCE = 40;
    private static final int SECOND_OVER_FARE_SECTION_START = 50;



    private int distanceBasic;
    private int distanceOver10km;
    private int distanceOver50km;
    private int fareByLine;

    public FareCalculator(int distance, int fareByLine) {
        this.distanceBasic = Math.min(distance, FIRST_OVER_FARE_SECTION_START);
        this.distanceOver10km = Math.min(Math.max(distance - FIRST_OVER_FARE_SECTION_START, 0), FIRST_OVER_FARE_SECTION_MAX_DISTANCE);
        this.distanceOver50km = Math.max(distance - SECOND_OVER_FARE_SECTION_START, 0);
        this.fareByLine = fareByLine;
    }

    public int fare() {
        return BASE_FARE + fareByLine + new OverFareCalculator(OVER_FARE_INTERVAL_UNTIL_50KM, OVER_FARE).calculateOverFare(this.distanceOver10km)
                + new OverFareCalculator(OVER_FARE_INTERVAL_AFTER_50KM, OVER_FARE).calculateOverFare(this.distanceOver50km);
    }
}
