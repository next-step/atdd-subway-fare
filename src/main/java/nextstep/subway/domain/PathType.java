package nextstep.subway.domain;

import java.util.function.Function;

public enum PathType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration);

    private Function<Section, Integer> weightFunction;

    PathType(Function<Section, Integer> weightFunction) {
        this.weightFunction = weightFunction;
    }

    public int weight(Section section) {
        return weightFunction.apply(section);
    }
}
