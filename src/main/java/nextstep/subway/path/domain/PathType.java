package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;

public enum PathType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration);

    private final PathFunction pathFunction;

    PathType(PathFunction pathFunction) {
        this.pathFunction = pathFunction;
    }

    public int value(Section section) {
        return pathFunction.value(section);
    }
}
