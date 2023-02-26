package nextstep.subway.domain;

public enum PathSearchType {
    DISTANCE,
    DURATION;

    public boolean isDistance() {
        return this == DISTANCE;
    }
}