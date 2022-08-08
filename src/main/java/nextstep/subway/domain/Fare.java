package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum Fare {
    STANDARD(distance -> distance <= Fare.STANDARD_DISTANCE, Fare.STANDARD_CRITERIA),
    TEN_OVER(distance -> distance > Fare.STANDARD_DISTANCE && distance <= Fare.FIFTY_DISTANCE, Fare.TEN_OVER_CRITERIA),
    FIFTY_OVER(distance -> distance > Fare.FIFTY_DISTANCE, Fare.FIFTY_OVER_CRITERIA);

    private final Predicate<Integer> predicate;
    private final int criteria;
    private final static int STANDARD_FARE = 1_250;
    private final static int STANDARD_DISTANCE = 10;
    private final static int STANDARD_CRITERIA = 0;
    private final static int FIFTY_DISTANCE = 50;
    private final static int TEN_OVER_CRITERIA = 5;
    private final static int FIFTY_OVER_CRITERIA = 8;

    Fare(Predicate<Integer> predicate, int criteria) {
        this.predicate = predicate;
        this.criteria = criteria;
    }

    public static int calculate(int distance, int higherSurCharge, int userAge) {
        int fare = calculateByDistance(distance) + higherSurCharge;
        return DiscountPolicy.calculate(userAge, fare);
    }

    private static int calculateByDistance(final int distance) {
        if (isStandard(distance)) {
            return STANDARD_FARE;
        }
        return STANDARD_FARE + calculateOverFare(distance, findType(distance).criteria);
    }

    private static boolean isStandard(int distance) {
        return findType(distance).equals(STANDARD);
    }

    private static int calculateOverFare(final int distance, final int criteria) {
        return (int) ((Math.ceil((distance - 1) / criteria) + 1) * 100);
    }

    private static Fare findType(int distance) {
        return Arrays.stream(Fare.values())
                .filter(fare -> fare.match(distance))
                .findFirst()
                .orElse(STANDARD);
    }

    private boolean match(int distance) {
        return predicate.test(distance);
    }
}
