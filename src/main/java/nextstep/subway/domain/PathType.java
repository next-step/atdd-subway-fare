package nextstep.subway.domain;

import java.util.function.Function;

public enum PathType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration);

    private final Function<Section, Integer> weightProvider;

    PathType(Function<Section, Integer> weightProvider) {
        this.weightProvider = weightProvider;
    }

    public int getWeight(Section section) {
        return weightProvider.apply(section);
    }
}
