package nextstep.subway.domain;

public enum DistanceRange {

    DEFAULT(0, 0),
    OVER_10_AND_UNDER_50(5, 100),
    OVER_50(8, 100),
    ;

    private final int intervalDistance;
    private final int amountPerDistance;

    DistanceRange(final int intervalDistance, final int amountPerDistance) {
        this.intervalDistance = intervalDistance;
        this.amountPerDistance = amountPerDistance;
    }

    public int getIntervalDistance() {
        return intervalDistance;
    }

    public int getAmountPerDistance() {
        return amountPerDistance;
    }
}
