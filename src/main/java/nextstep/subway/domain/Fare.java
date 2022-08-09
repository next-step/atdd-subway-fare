package nextstep.subway.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Fare {
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int DEFAULT_FARE = 1250;
    private static final int ADDITIONAL_FARE = 800;
    private static final int PER_FARE = 100;
    private static final int FIRST_REFERENCE_DISTANCE = 10;
    private static final int SECOND_REFERENCE_DISTANCE = 50;
    private static final int FIRST_REFERENCE = 5;
    private static final int SECOND_REFERENCE = 8;
    public final int fare;

    public Fare(int distance) {
        int fare = DEFAULT_FARE;
        if (FIRST_REFERENCE_DISTANCE < distance) {
            fare += calculateOverFare(distance);
        }
        this.fare = fare;
    }

    public int getFare() {
        return fare;
    }

    private static int calculateOverFare(int distance) {
        if (distance <= SECOND_REFERENCE_DISTANCE) {
            BigDecimal firstOverFare = getScale(distance - FIRST_REFERENCE_DISTANCE, FIRST_REFERENCE);
            return firstOverFare.intValue() * PER_FARE;
        }

        BigDecimal secondOverFare = getScale(distance - SECOND_REFERENCE_DISTANCE, SECOND_REFERENCE);
        return ADDITIONAL_FARE + secondOverFare.intValue() * PER_FARE;
    }

    private static BigDecimal getScale(int distance, int x) {
        return BigDecimal.valueOf((distance - ONE) / x + ONE).setScale(ZERO, RoundingMode.CEILING);
    }
}
