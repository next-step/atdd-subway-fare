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
    private static final int BASIC_ADDITIONAL_DISTANCE_FARE = 2050;

    @Override
    public int calculate(final int distance) {
        if (distance < DISTANCE_MIN) {
            throw new DistanceMinException(DISTANCE_MIN);
        }
        if (distance <= BASIC_ADDITIONAL_DISTANCE_LIMIT) {
            return BASE_FARE + calculateAdditionalFare(distance - BASE_DISTANCE, BASIC_ADDITIONAL_DISTANCE);
        }
        return BASIC_ADDITIONAL_DISTANCE_FARE
                + calculateAdditionalFare(distance - BASIC_ADDITIONAL_DISTANCE_LIMIT, EXTRA_ADDITIONAL_DISTANCE);
    }

    private int calculateAdditionalFare(
            final int distance,
            final int basicAdditionalDistance
    ) {
        if (distance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / basicAdditionalDistance) + 1) * ADDITIONAL_FARE);
    }
}
