package nextstep.subway.domain;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class FareUtils {
    private static final int DEFAULT_FARE = 1_250;
    private static final int EXTRA_FARE = 100;
    private static final int DEFAULT_DISTANCE = 10;
    private static final int EXTRA_DISTANCE = 50;
    private static final int EXTRA_FARE_ONE = 5;
    private static final int EXTRA_FARE_TWO = 8;

    public static int getFare(final int distance) {
        if (distance <= DEFAULT_DISTANCE) {
            return DEFAULT_FARE;
        }
        if (distance <= EXTRA_DISTANCE) {
            return DEFAULT_FARE + calculateOverFare((distance - DEFAULT_DISTANCE), EXTRA_FARE_ONE);
        }
        if (distance <= Integer.MAX_VALUE) {
            int overSectionTwo = distance - EXTRA_DISTANCE;

            return DEFAULT_FARE
                    + calculateOverFare(EXTRA_DISTANCE - DEFAULT_FARE, EXTRA_FARE_ONE)
                    + calculateOverFare(overSectionTwo, EXTRA_FARE_TWO);
        }

        throw new AssertionError("거리 계산이 잘못되었습니다.");
    }

    private static int calculateOverFare(final int distance, final int extraFareRate) {
        return (int) ((Math.ceil((distance - 1) / extraFareRate) + 1) * EXTRA_FARE);
    }
}
