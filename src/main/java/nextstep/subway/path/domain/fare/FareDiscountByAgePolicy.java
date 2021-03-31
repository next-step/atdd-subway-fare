package nextstep.subway.path.domain.fare;

import java.util.function.Function;

public enum FareDiscountByAgePolicy {

    ADULT(totalFare -> 0),
    YOUTH(totalFare -> (int) Math.round((totalFare - 350) * 0.2)),
    CHILD(totalFare -> (int) Math.round((totalFare - 350) * 0.5)),
    INFANT(totalFare -> totalFare);

    private final Function<Integer, Integer> operation;

    FareDiscountByAgePolicy(Function<Integer, Integer> operation) {
        this.operation = operation;
    }

    public Function<Integer, Integer> getOperation() {
        return operation;
    }

    public static FareDiscountByAgePolicy byAge(int age) {
        if (age >= 19) {
            return ADULT;
        }

        if (age >= 13) {
            return YOUTH;
        }

        if (age >= 6) {
            return CHILD;
        }

        return INFANT;
    }
}
