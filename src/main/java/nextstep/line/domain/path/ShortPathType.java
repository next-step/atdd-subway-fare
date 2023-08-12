package nextstep.line.domain.path;

public enum ShortPathType {

    DISTANCE, DURATION;

    public boolean isDistance() {
        return this == ShortPathType.DISTANCE;
    }

    public boolean isDuration() {
        return this == ShortPathType.DURATION;
    }

}
