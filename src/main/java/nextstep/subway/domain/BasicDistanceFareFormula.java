package nextstep.subway.domain;

import nextstep.subway.domain.exception.DistanceMinException;

public class BasicDistanceFareFormula implements DistanceFareFormula {
    private static final int DISTANCE_MIN = 1;
    private static final int BASE_DISTANCE = 10;
    private static final int BASE_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;

    @Override
    public int calculate(final int distance) {
        if (distance < DISTANCE_MIN) {
            throw new DistanceMinException(DISTANCE_MIN);
        }
        if (distance < BASE_DISTANCE) {
            return BASE_FARE;
        }
        return calculateAdditionalFare(distance);
    }

    private int calculateAdditionalFare(final Integer distance) {
        int temp = BASE_DISTANCE;
        int fare = BASE_FARE;
        while (temp < distance) {
            if (temp < 50) {
                temp += 5;
                fare += ADDITIONAL_FARE;
                continue;
            }
            temp += 8;
            fare += ADDITIONAL_FARE;
        }
        return fare;
    }
}
