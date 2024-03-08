package nextstep.subway.line.path;

public enum PathType {
    DISTANCE,
    DURATION;

    public boolean isDistance(){
        return this.equals(DISTANCE);
    }
}
