package nextstep.subway.domain;

import static nextstep.subway.domain.Fare.ZERO_FARE;

public class OverDistanceFarePolicy {

    private static int FIRST_RANGE_DISTANCE = 10;
    private static int FIRST_RANGE_PER_KM = 5;

    private static int SECOND_RANGE_DISTANCE = 50;
    private static int SECOND_RANGE_PER_KM = 8;
    private static int PER_FARE = 100;

    public static Fare calculateOverDistanceFare(int distance) {
        Fare fare = ZERO_FARE;

        if (distance > FIRST_RANGE_DISTANCE) {
            int overDistance = Math.min(SECOND_RANGE_DISTANCE, distance) - FIRST_RANGE_DISTANCE;
            fare = fare.plus(calculate(overDistance, FIRST_RANGE_PER_KM));
        }

        if (distance > SECOND_RANGE_DISTANCE) {
            int overDistance = distance - SECOND_RANGE_DISTANCE;
            fare = fare.plus(calculate(overDistance, SECOND_RANGE_PER_KM));
        }

        return fare;
    }

    private static Fare calculate(int overDistance, int perKm) {
        int overFare = (int) (Math.ceil((overDistance + perKm - 1) / perKm) * PER_FARE);
        return Fare.of(overFare);
    }
}
