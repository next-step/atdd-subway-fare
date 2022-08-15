package nextstep.subway.domain;

import java.util.function.Function;

public enum PathType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration);

    private Function<Section, Integer> value;

    PathType(Function<Section, Integer> value) {
        this.value = value;
    }

    public Integer getValue(Section section) {
        return value.apply(section);
    }


}
