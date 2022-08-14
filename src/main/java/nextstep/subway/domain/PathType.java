package nextstep.subway.domain;

import nextstep.subway.domain.strategy.WeightStrategy;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public enum PathType {

    DURATION(Section::getDuration),
    DISTANCE(Section::getDistance);

    private static final Map<String, PathType> pathTypeMap;
    private final WeightStrategy weightStrategy;

    static {
        pathTypeMap = Arrays.stream(values()).collect(Collectors.toMap(PathType::name, pathType -> pathType));
    }

    PathType(WeightStrategy weightStrategy) {
        this.weightStrategy = weightStrategy;
    }

    public static PathType of(String value) {
        PathType type = pathTypeMap.get(value);
        if (Objects.isNull(type)) {
            throw new IllegalArgumentException();
        }
        return type;
    }

    public WeightStrategy weightStrategy() {
        return this.weightStrategy;
    }

    public boolean isDistanceType() {
        return this == DISTANCE;
    }
}
