package nextstep.subway.domain;

public enum OverFareLevel {

    LEVEL1(5, 10),
    LEVEL2(8, 50);

    private final int interval;
    private final int limit;

    OverFareLevel(int interval, int limit) {
        this.interval = interval;
        this.limit = limit;
    }

    public int getInterval() {
        return interval;
    }

    public int getLimit() {
        return limit;
    }
}
