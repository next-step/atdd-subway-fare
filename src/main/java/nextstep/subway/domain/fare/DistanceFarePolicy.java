package nextstep.subway.domain.fare;

import nextstep.subway.domain.fare.distance.OverFareCalculator;

public class DistanceFarePolicy extends FarePolicy {

    private static final int OVER_FARE = 100;
    private static final int OVER_FARE_INTERVAL_UNTIL_50KM = 5;
    private static final int OVER_FARE_INTERVAL_AFTER_50KM = 8;

    private static final int FIRST_OVER_FARE_SECTION_START = 10;
    private static final int FIRST_OVER_FARE_SECTION_MAX_DISTANCE = 40;
    private static final int SECOND_OVER_FARE_SECTION_START = 50;

    private final int distanceOver10km;
    private final int distanceOver50km;

    public DistanceFarePolicy(int distance) {
        this.distanceOver10km = Math.min(Math.max(distance - FIRST_OVER_FARE_SECTION_START, 0), FIRST_OVER_FARE_SECTION_MAX_DISTANCE);
        this.distanceOver50km = Math.max(distance - SECOND_OVER_FARE_SECTION_START, 0);
    }

    @Override
    public int fare(int prevFare) {
        return prevFare + new OverFareCalculator(OVER_FARE_INTERVAL_UNTIL_50KM, OVER_FARE).calculateOverFare(this.distanceOver10km)
                + new OverFareCalculator(OVER_FARE_INTERVAL_AFTER_50KM, OVER_FARE).calculateOverFare(this.distanceOver50km);
    }
}
