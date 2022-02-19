package nextstep.subway.domain;

public enum PathType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration);

    private PathFunction pathFunction;

    PathType(PathFunction pathFunction) {
        this.pathFunction = pathFunction;
    }

    public int value(Section section) {
        return pathFunction.value(section);
    }
}
