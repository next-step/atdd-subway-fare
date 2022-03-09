package nextstep.subway.domain;

import static java.util.Arrays.stream;

public enum FareStandard {
    FARE50(51, 9999, 8, 2_050, 100),
    FARE10(11, 50, 5, 1_250, 100),
    FARE_DEFAULT(0, 10, 5, 1_250, 0);

    private final int beginBoundary;
    private final int endBoundary;
    private final int distance;
    private final int basicFare;
    private final int percentage;

    FareStandard(int beginBoundary, int endBoundary, int distance, int basicFare, int percentage) {
        this.beginBoundary = beginBoundary;
        this.endBoundary = endBoundary;
        this.distance = distance;
        this.basicFare = basicFare;
        this.percentage = percentage;
    }

    public static int calculateOverFare(int distance) {
        FareStandard fareStandard = valueOf(distance);

        int overDistance = distance - (fareStandard.beginBoundary - 1);
        return ((overDistance - 1) / fareStandard.distance + 1) * fareStandard.percentage + fareStandard.basicFare;
    }

    private static FareStandard valueOf(int distance) {
        return stream(values())
                .filter(value -> isBoundary(distance, value))
                .findFirst()
                .orElse(FARE_DEFAULT);
    }

    private static boolean isBoundary(int distance, FareStandard value) {
        return distance >= value.beginBoundary && distance <= value.endBoundary;
    }
}
