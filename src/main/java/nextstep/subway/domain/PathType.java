package nextstep.subway.domain;

public enum PathType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration);

    private final PathFunction function;

    PathType(PathFunction function) {
        this.function = function;
    }

    public int getValue(Section section) {
        return function.getValue(section);
    }
}
