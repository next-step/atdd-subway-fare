package nextstep.subway.path.domain;

public enum PathType {
    DISTANCE,
    DURATION;

    public boolean isDistance(){
        return this.equals(DISTANCE);
    }
}
