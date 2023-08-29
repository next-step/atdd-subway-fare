package nextstep.subway.path.domain.chainofresponsibility.fare;

import nextstep.subway.path.domain.Path;

public class DistanceFarePolicy implements FarePolicy {
    private static final int BASIC_FEE = 1250;
    private static final int SHORT_DISTANCE_LIMIT = 10;
    private static final int MEDIUM_DISTANCE_LIMIT = 50;
    private static final int MEDIUM_DISTANCE_UNIT = 5;
    private static final int LONG_DISTANCE_UNIT = 8;
    private static final int OVER_FARE = 100;

    @Override
    public int calculateFare(Path path) {
        int totalDistance = path.getTotalDistance();
        if (totalDistance <= SHORT_DISTANCE_LIMIT) {  // Rule로 추상화 해보자
            return BASIC_FEE;
        }

        if (totalDistance <= MEDIUM_DISTANCE_LIMIT) {
            int lastDistance = totalDistance - SHORT_DISTANCE_LIMIT;
            return BASIC_FEE + calculateOverFare(lastDistance, MEDIUM_DISTANCE_UNIT);
        }

        int lastDistance = totalDistance - MEDIUM_DISTANCE_LIMIT;
        int middleDistance = totalDistance - lastDistance - SHORT_DISTANCE_LIMIT;

        return BASIC_FEE + calculateOverFare(middleDistance, MEDIUM_DISTANCE_UNIT) + calculateOverFare(lastDistance, LONG_DISTANCE_UNIT);
    }

    private int calculateOverFare(int distance, int distanceUnit) {
        return ((distance - 1) / distanceUnit + 1) * OVER_FARE;
    }
}
