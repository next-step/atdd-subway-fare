package nextstep.subway.domain;

import java.util.function.Function;

public enum PathSearchType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration)
    ;

    private final Function<Section, Integer> function;

    PathSearchType(Function<Section, Integer> function) {
        this.function = function;
    }

    public int getWeight(Section section) {
        return function.apply(section);
    }
}
