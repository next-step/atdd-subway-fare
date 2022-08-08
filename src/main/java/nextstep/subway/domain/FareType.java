package nextstep.subway.domain;

public enum FareType {
    MIDDLE_DISTANCE, LONG_DISTANCE, BASIC_DISTANCE;

    private static final String INVALID_DISTANCE_EXCEPTION = "거리는 양수이여야만 합니다.";

    public static FareType calculateType(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException(INVALID_DISTANCE_EXCEPTION);
        } else if (distance <= 10) {
            return BASIC_DISTANCE;
        } else if (distance <= 50) {
            return MIDDLE_DISTANCE;
        } else {
            return LONG_DISTANCE;
        }
    }
}
