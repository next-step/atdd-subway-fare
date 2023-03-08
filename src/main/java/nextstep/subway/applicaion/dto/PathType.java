package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Section;

import java.util.function.Function;

public enum PathType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration),
    ;

    private final Function<Section, Integer> getTypeValue;

    PathType(Function<Section, Integer> getTypeValue) {
        this.getTypeValue = getTypeValue;
    }

    public int getTypeValue(final Section section) {
        return getTypeValue.apply(section);
    }
}
