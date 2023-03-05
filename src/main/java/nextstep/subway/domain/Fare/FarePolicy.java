package nextstep.subway.domain.Fare;

import java.util.Arrays;

public class FarePolicy {
    private final static int DEFAULT_FARE = 1250;
    private final static int EXTRA_FARE = 100;
    private final static int FIRST_FARE_SECTION = 10;
    private final static int SECOND_FARE_SECTION = 50;
    private static final int DEDUCTED_AMOUNT = 350;
    private static final int ADULT_AGE = 19;
    private static final int TEENAGER_MIN_AGE = 13;
    private static final int TEENAGER_MAX_AGE = 18;
    private static final int CHILD_MIN_AGE = 6;
    private static final int CHILD_MAX_AGE = 12;
    private static final double TEENAGER_DISCOUNT_RATE = 0.2;
    private static final double CHILD_DISCOUNT_RATE = 0.5;

    public static int getFare(int distance) {
        int fare = DEFAULT_FARE;
        if (distance > FIRST_FARE_SECTION && distance <= SECOND_FARE_SECTION) {
            fare += (int) ((Math.ceil((distance - FIRST_FARE_SECTION -1) / 5) + 1) * EXTRA_FARE);
        }
        if (distance > SECOND_FARE_SECTION) {
            fare += (int) (800 + (Math.ceil((distance - SECOND_FARE_SECTION - 1) / 8) + 1) * EXTRA_FARE);
        }
        return fare;
    }

    public static int discountFare(int fare, int age) {
        double discountRate = 0;
        if (age >= CHILD_MIN_AGE && age <= CHILD_MAX_AGE) {
            discountRate = CHILD_DISCOUNT_RATE;
        }
        if (age >= TEENAGER_MIN_AGE && age <= TEENAGER_MAX_AGE) {
            discountRate = TEENAGER_DISCOUNT_RATE;
        }
        return (int) (fare - (fare - DEDUCTED_AMOUNT) * discountRate);
    }
}
