package nextstep.subway.domain;

import java.util.function.Function;

public enum PathType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration);

    private final Function<Section, Integer> weight;

    PathType(Function<Section, Integer> weight) {
        this.weight = weight;
    }

    public int findWeight(Section section) {
        return weight.apply(section);
    }
}
