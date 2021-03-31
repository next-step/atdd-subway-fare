package nextstep.subway.path.domain.fare;

import java.util.function.Function;

public enum FareByDistancePolicy {

    SECTION_UNDER_10(distance -> 0),
    SECTION_10_TO_50(distance -> (int) ((Math.ceil((distance - 10 - 1) / 5) + 1) * 100)),
    SECTION_OVER_50(distance -> 800 + (int) ((Math.ceil((distance - 50 - 1) / 8) + 1) * 100));

    private final Function<Integer, Integer> operation;

    FareByDistancePolicy(Function<Integer, Integer> operation) {
        this.operation = operation;
    }

    public Function<Integer, Integer> getOperation() {
        return operation;
    }

    public static FareByDistancePolicy byDistance(int distance) {
        if (distance > 50) {
            return SECTION_OVER_50;
        }

        if (distance > 10) {
            return SECTION_10_TO_50;
        }

        return SECTION_UNDER_10;
    }
}
