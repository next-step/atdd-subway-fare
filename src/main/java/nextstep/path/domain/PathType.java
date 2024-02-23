package nextstep.path.domain;

import java.util.function.Function;

public enum PathType {
    DISTANCE(PathSection::getDistance), DURATION(PathSection::getDuration);

    private final Function<PathSection, Integer> weightStrategy;

    PathType(final Function<PathSection, Integer> weightStrategy) {
        this.weightStrategy = weightStrategy;
    }

    public int calculateWeight(final PathSection pathSection) {
        return weightStrategy.apply(pathSection);
    }
}
