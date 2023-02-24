package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum Fare {
    BASIC(
        distance -> distance <= Fare.BASIC_DISTANCE,
        distance -> 0
    ),
    MIDDLE(
        distance -> distance > Fare.BASIC_DISTANCE && distance <= Fare.MIDDLE_DISTANCE,
        distance -> calculateExtraFare(distance, Fare.UNIT_BELOW_MIDDLE_DISTANCE)
    ),
    LONG(
        distance -> distance > Fare.MIDDLE_DISTANCE,
        distance -> calculateExtraFare(distance, Fare.UNIT_OVER_MIDDLE_DISTANCE)
    );

    private static final int BASIC_DISTANCE = 10;
    private static final int MIDDLE_DISTANCE = 50;
    private static final int BASIC_FARE = 1_250;
    private static final int EXTRA_FARE_PER_UNIT_DISTANCE = 100;
    private static final int UNIT_BELOW_MIDDLE_DISTANCE = 5;
    private static final int UNIT_OVER_MIDDLE_DISTANCE = 8;

    private final Predicate<Integer> predicate;
    private final UnaryOperator<Integer> extraFareOperator;

    Fare(Predicate<Integer> predicate, UnaryOperator<Integer> extraFareOperator) {
        this.predicate = predicate;
        this.extraFareOperator = extraFareOperator;
    }

    public static int calculate(int distance, int lineExtraFare, int age) {
        int extraFare = Arrays.stream(values())
            .filter(it -> it.predicate.test(distance))
            .findFirst()
            .orElse(BASIC)
            .extraFareOperator.apply(distance);
        int fare = BASIC_FARE + extraFare + lineExtraFare;
        return DiscountPolicy.discount(fare, age);
    }

    private static int calculateExtraFare(int distance, int unit) {
        return (int) ((Math.ceil((distance - BASIC_DISTANCE - 1) / unit) + 1) * EXTRA_FARE_PER_UNIT_DISTANCE);
    }
}
