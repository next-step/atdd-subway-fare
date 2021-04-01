package nextstep.subway.path.domain.fare;

import java.util.Arrays;
import java.util.function.Function;

public enum FareDiscountByAgePolicy {

    ADULT(
            age -> age >= 19,
            totalFare -> 0
    ),
    YOUTH(
            age -> age >= 13 && age < 19,
            totalFare -> (int) Math.round((totalFare - 350) * 0.2)
    ),
    CHILD(
            age -> age >= 6 && age < 13,
            totalFare -> (int) Math.round((totalFare - 350) * 0.5)
    ),
    INFANT(
            age -> age < 6,
            totalFare -> totalFare
    );

    private final Function<Integer, Boolean> condition;
    private final Function<Integer, Integer> operation;

    FareDiscountByAgePolicy(Function<Integer, Boolean> condition, Function<Integer, Integer> operation) {
        this.condition = condition;
        this.operation = operation;
    }

    public Function<Integer, Integer> getOperation() {
        return operation;
    }

    public Function<Integer, Boolean> getCondition() {
        return condition;
    }

    public static FareDiscountByAgePolicy byAge(int age) {
        return Arrays.stream(values())
                .filter(it -> it.getCondition().apply(age))
                .findFirst()
                .orElse(ADULT);
    }
}
