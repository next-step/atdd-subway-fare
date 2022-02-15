package nextstep.subway.domain;

import java.util.function.Function;

public enum PathType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration);

    private Function<Section, Integer> weight;

    PathType(Function<Section, Integer> function) {
        this.weight = function;
    }

    public double getWeight(Section section) {
        return weight.apply(section);
    }
}
