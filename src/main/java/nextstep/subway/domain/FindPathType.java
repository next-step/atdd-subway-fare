package nextstep.subway.domain;

public enum FindPathType {
    DISTANCE, DURATION;

    public boolean isDistanceType() {
        return this.equals(DISTANCE);
    }
}
