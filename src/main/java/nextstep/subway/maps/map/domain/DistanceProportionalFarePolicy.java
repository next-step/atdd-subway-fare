package nextstep.subway.maps.map.domain;

import org.springframework.lang.Nullable;

public class DistanceProportionalFarePolicy implements FarePolicy {
    public static final int DEFAULT_FARE = 1250;
    private static final int OVER_DISTANCE_FARE_UNIT = 100;

    private static final int FIRST_DISTANCE_SECTION_MIN = 10;
    private static final int FIRST_DISTANCE_SECTION_MAX = 50;

    private static final int SECOND_DISTANCE_SECTION_MIN = 50;

    private static final int FIRST_UNIT_DISTANCE = 5;
    private static final int SECOND_UNIT_DISTANCE = 8;

    @Override
    public int calculate(FareContext fareContext) {
        int fare = DEFAULT_FARE;
        int distance = fareContext.getDistance();
        fare += calculateOverFare(distance, FIRST_DISTANCE_SECTION_MIN, FIRST_DISTANCE_SECTION_MAX, FIRST_UNIT_DISTANCE);
        fare += calculateOverFare(distance, SECOND_DISTANCE_SECTION_MIN, null, SECOND_UNIT_DISTANCE);

        return fare;
    }

    private int calculateOverFare(Integer distance, Integer min, @Nullable Integer max, Integer unitDistance) {
        if (distance <= min) {
            return 0;
        }

        if (max != null && distance > max) {
            return calculateOverFare(max - min, unitDistance);
        }

        int overDistance = distance - min;
        return calculateOverFare(overDistance, unitDistance);
    }

    private int calculateOverFare(int overDistance, int unitDistance) {
        return (int) ((Math.ceil((overDistance - 1) / unitDistance) + 1) * OVER_DISTANCE_FARE_UNIT);
    }
}
