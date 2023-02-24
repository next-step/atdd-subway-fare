package nextstep.subway.domain;

public enum PathRequestType {
    DISTANCE, DURATION;

    public boolean isDuration() {
        return this.equals(DURATION);
    }
}
