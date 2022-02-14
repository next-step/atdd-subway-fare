package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum FareType {

    BASIC(distance -> distance <= 10, 0),
    PER_FIVE(distance -> distance > 10 && distance <= 50, 5),
    PER_EIGHT(distance -> distance > 50, 8);

    private static final int BASIC_FARE = 1_250;

    private final Predicate<Integer> predicate;
    private final int per;

    FareType(Predicate<Integer> predicate, int per) {
        this.predicate = predicate;
        this.per = per;
    }

    public static FareType from(int distance) {
        return Arrays.stream(FareType.values())
                .filter(fareType -> fareType.match(distance))
                .findFirst()
                .orElse(BASIC);
    }

    private boolean match(int distance) {
        return predicate.test(distance);
    }

    public static int fare(int distance) {
        return from(distance).calculate(distance);
    }

    private int calculate(int distance) {
        if (this == BASIC) {
            return BASIC_FARE;
        }

        int extraCharge = (int) ((Math.ceil(((double) distance - 11) / per) + 1) * 100);
        return BASIC_FARE + extraCharge;
    }

}
