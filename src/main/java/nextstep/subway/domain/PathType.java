package nextstep.subway.domain;

import nextstep.subway.domain.strategy.WeightStrategy;

import java.util.stream.Stream;

public enum PathType {

    DURATION(Section::getDuration),
    DISTANCE(Section::getDistance);

    private final WeightStrategy weightStrategy;

    PathType(WeightStrategy weightStrategy) {
        this.weightStrategy = weightStrategy;
    }

    public static PathType of(String value) {
        return Stream.of(values())
                .filter(pathType -> pathType.name().equals(value))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public WeightStrategy weightStrategy() {
        return this.weightStrategy;
    }
}
