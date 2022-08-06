package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum Fare {
    STANDARD(distance -> distance <= 10, 0),
    TEN_OVER(distance -> distance > 10 && distance <= 50, 5),
    FIFTY_OVER(distance -> distance > 50, 8);

    private final Predicate<Integer> predicate;
    private final int criteria;
    private final static int STANDARD_FARE = 1_250;

    Fare(Predicate<Integer> predicate, int criteria) {
        this.predicate = predicate;
        this.criteria = criteria;
    }

    public static int calculate(final int distance) {
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
