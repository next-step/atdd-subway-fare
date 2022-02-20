package nextstep.subway.domain;

public enum FareStandard {
    FARE50(50, 8, 2_050),
    FARE10(10, 5, 1_250);

    private final int boundary;
    private final int distance;
    private final int basicFare;

    FareStandard(int boundary, int distance, int basicFare) {
        this.boundary = boundary;
        this.distance = distance;
        this.basicFare = basicFare;
    }

    public static int calculateOverFare(int distance) {
        FareStandard fareStandard = of(distance);
        if (distance <= fareStandard.boundary) {
            return fareStandard.basicFare;
        }

        int overDistance = distance - fareStandard.boundary;
        return ((overDistance - 1) / fareStandard.distance + 1) * 100 + fareStandard.basicFare;
        // 이런 힌트를 주셨는데 뭐지?
//        return (int) ((ceil((overDistance - 1) / 5) + 1) * 100) + BASIC_FARE;
    }

    private static FareStandard of(int distance) {
        if (distance > FARE50.boundary) {
            return FARE50;
        }
        return FARE10;
    }
}
