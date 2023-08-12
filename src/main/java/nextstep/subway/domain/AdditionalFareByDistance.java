package nextstep.subway.domain;

public class AdditionalFareByDistance implements FarePolicy {

    private static final int OVER_FARE = 100;
    private static final int OVER_FARE_INTERVAL_UNTIL_50KM = 5;
    private static final int OVER_FARE_INTERVAL_AFTER_50KM = 8;

    private static final int FIRST_OVER_FARE_SECTION_START = 10;
    private static final int FIRST_OVER_FARE_SECTION_MAX_DISTANCE = 40;
    private static final int SECOND_OVER_FARE_SECTION_START = 50;

    private int distanceOver10km;
    private int distanceOver50km;
    private FarePolicy next;

    public AdditionalFareByDistance(int distance) {
        this.distanceOver10km = Math.min(Math.max(distance - FIRST_OVER_FARE_SECTION_START, 0), FIRST_OVER_FARE_SECTION_MAX_DISTANCE);
        this.distanceOver50km = Math.max(distance - SECOND_OVER_FARE_SECTION_START, 0);
    }

    @Override
    public void setNext(FarePolicy next) {
        this.next = next;
    }

    @Override
    public int fare(int prevFare) {
        int fare = prevFare + new OverFareCalculator(OVER_FARE_INTERVAL_UNTIL_50KM, OVER_FARE).calculateOverFare(this.distanceOver10km) + new OverFareCalculator(OVER_FARE_INTERVAL_AFTER_50KM, OVER_FARE).calculateOverFare(this.distanceOver50km);
        return next != null ? next.fare(fare) : fare;
    }
}
