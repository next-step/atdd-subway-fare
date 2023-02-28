package nextstep.subway.domain;

public enum SearchType {
    DISTANCE,
    DURATION,
    ;

    public boolean isDistance() {
        return this.equals(DISTANCE);
    }

    public boolean isDuration() {
        return this.equals(DURATION);
    }
}
