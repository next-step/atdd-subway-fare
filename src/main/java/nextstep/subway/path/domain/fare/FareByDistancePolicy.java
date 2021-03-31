package nextstep.subway.path.domain.fare;

import java.util.Arrays;
import java.util.function.Function;

public enum FareByDistancePolicy {

    SECTION_UNDER_10_KM(
            distance -> distance <= 10,
            distance -> 0
    ),
    SECTION_10_KM_TO_50_KM(
            distance -> distance > 10 && distance <= 50,
            distance -> (int) ((Math.ceil((distance - 10 - 1) / 5) + 1) * 100)
    ),
    SECTION_OVER_50_KM(
            distance -> distance > 50,
            distance -> 800 + (int) ((Math.ceil((distance - 50 - 1) / 8) + 1) * 100)
    );

    private final Function<Integer, Boolean> condition;
    private final Function<Integer, Integer> operation;

    FareByDistancePolicy(Function<Integer, Boolean> condition, Function<Integer, Integer> operation) {
        this.condition = condition;
        this.operation = operation;
    }

    public Function<Integer, Integer> getOperation() {
        return operation;
    }

    public Function<Integer, Boolean> getCondition() {
        return condition;
    }

    public static FareByDistancePolicy byDistance(int distance) {
        return Arrays.stream(values())
                .filter(it -> it.getCondition().apply(distance))
                .findFirst()
                .orElse(SECTION_UNDER_10_KM);
    }
}
