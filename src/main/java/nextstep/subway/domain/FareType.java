package nextstep.subway.domain;

public enum FareType {

    BASIC,
    PER_FIVE,
    PER_EIGHT;

    public static FareType from(int distance) {
        return BASIC;
    }

    public int fare() {
        return 0;
    }

}
