package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Function;
import nextstep.subway.domain.exception.PathTypeNotFoundException;

public enum PathType {
    DISTANCE(Section::getDistance);

    private final Function<Section, Integer> graphWeightStrategy;

    PathType(final Function<Section, Integer> graphWeightStrategy) {
        this.graphWeightStrategy = graphWeightStrategy;
    }

    public static PathType findBy(final String name) {
        return Arrays.stream(PathType.values())
                .filter(pathType -> pathType.name().equals(name))
                .findFirst()
                .orElseThrow(PathTypeNotFoundException::new);
    }

    public Function<Section, Integer> getStrategy() {
        return graphWeightStrategy;
    }
}
