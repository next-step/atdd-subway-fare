package nextstep.path;

import nextstep.section.Section;

import java.util.function.Function;

public enum PathType {

    DISTANCE(Section::getDistance), DURATION(Section::getDuration);

    private final Function<Section, Integer> weightStrategy;

    PathType(final Function<Section, Integer> weightStrategy) {
        this.weightStrategy = weightStrategy;
    }

    public int getWeight(final Section section) {
        return weightStrategy.apply(section);
    }
}
