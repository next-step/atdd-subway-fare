package nextstep.subway.domain.enums;

public enum PathType {

    DISTANCE, DURATION;

    public boolean isDistance() {
        return this == DISTANCE;
    }
}
