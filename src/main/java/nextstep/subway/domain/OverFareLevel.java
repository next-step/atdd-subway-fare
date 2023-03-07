package nextstep.subway.domain;

public enum OverFareLevel {

    OVER_10KM(5, 10),
    OVER_50KM(8, 50);

    private final int interval;
    private final int start;

    OverFareLevel(int interval, int start) {
        this.interval = interval;
        this.start = start;
    }

    public int getInterval() {
        return interval;
    }

    public int getStart() {
        return start;
    }

    public boolean matches(int distance) {
        return distance <= this.start;
    }
}
