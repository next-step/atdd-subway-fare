package nextstep.subway.domain;

import java.util.function.Function;

public enum PathType {

    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration);

    private final Function<Section, Integer> function;

    PathType(Function<Section, Integer> function) {
        this.function = function;
    }

    public int getWeight(Section it) {
        return function.apply(it);
    }

}
