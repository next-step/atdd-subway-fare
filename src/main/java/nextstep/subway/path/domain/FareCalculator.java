package nextstep.subway.path.domain;

public class FareCalculator {
    private static final int BASIC_FARE = 1_250;
    private static final int ADDITIONAL_FARE = 100;
    private static final int DISTANCE_UNIT_1 = 10;
    private static final int DISTANCE_UNIT_2 = 50;
    private static final int ADDITIONAL_FARE_UNIT_2 = 5;
    private static final int ADDITIONAL_FARE_OVER_UNIT = 8;


    public static int calculateFare(int totalDistance) {
        if (totalDistance <= DISTANCE_UNIT_1) {
            return BASIC_FARE;
        }

        if (totalDistance <= DISTANCE_UNIT_2) {
            return calculateDistanceUnit2(totalDistance);
        }

        return calculateOverDistanceUnit(totalDistance);
    }

    private static int calculateDistanceUnit2(int totalDistance) {
        int additionalDistance = totalDistance - DISTANCE_UNIT_1;
        int additionalFare = calculateAdditionalFare(additionalDistance, ADDITIONAL_FARE_UNIT_2);
        return BASIC_FARE + additionalFare;
    }

    private static int calculateOverDistanceUnit(int totalDistance) {
        int additionalDistance = totalDistance - DISTANCE_UNIT_2;
        int additionalFare1 = calculateAdditionalFare((DISTANCE_UNIT_2 - DISTANCE_UNIT_1), DISTANCE_UNIT_1);
        return BASIC_FARE + additionalFare1 + calculateAdditionalFare(additionalDistance, ADDITIONAL_FARE_OVER_UNIT);
    }

    private static int calculateAdditionalFare(int distance, int unit) {
        return (int) ((Math.ceil((double) distance / unit)) * ADDITIONAL_FARE);
    }
}
