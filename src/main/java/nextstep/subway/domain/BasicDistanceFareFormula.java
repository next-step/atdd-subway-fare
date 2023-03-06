package nextstep.subway.domain;

import nextstep.subway.domain.exception.DistanceMinException;

public class BasicDistanceFareFormula implements DistanceFareFormula {
    private static final int DISTANCE_MIN = 1;
    private static final int BASE_DISTANCE = 10;
    private static final int BASE_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;
    private static final int BASIC_ADDITIONAL_DISTANCE_LIMIT = 50;
    private static final int BASIC_ADDITIONAL_DISTANCE = 5;
    private static final int EXTRA_ADDITIONAL_DISTANCE = 8;

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
            if (temp < BASIC_ADDITIONAL_DISTANCE_LIMIT) {
                temp += BASIC_ADDITIONAL_DISTANCE;
                fare += ADDITIONAL_FARE;
                continue;
            }
            temp += EXTRA_ADDITIONAL_DISTANCE;
            fare += ADDITIONAL_FARE;
        }
        return fare;
    }
}
