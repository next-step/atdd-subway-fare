package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum FareType {

    STANDARD(distance -> distance <= 10, 0),
    NORMAL(distance -> distance > 10 && distance <= 50, 5),
    LONGEST(distance -> distance > 50, 8),
    ;

    public static final int STANDARD_FARE = 1_250;

    private final Predicate<Integer> predicate;
    private final int criteria;

    FareType(Predicate<Integer> predicate, int criteria) {
        this.predicate = predicate;
        this.criteria = criteria;
    }

    public static boolean isStandard(int distance) {
        return findType(distance).equals(STANDARD);
    }

    public static int findCriteria(int distance) {
        return findType(distance).criteria;
    }

    private static FareType findType(int distance) {
        return Arrays.stream(FareType.values())
                .filter(fareType -> fareType.match(distance))
                .findFirst()
                .orElse(STANDARD);
    }

    private boolean match(int distance) {
        return predicate.test(distance);
    }
}
