package nextstep.subway.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum FindType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration);

    private final Function<Section, Integer> weightFunction;

    FindType(Function<Section, Integer> weightFunction) {
        this.weightFunction = weightFunction;
    }

    public static FindType from(String type) {
        return Arrays.stream(values())
                .filter(findType -> findType.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException());
    }

    public boolean isDistance() {
        return this == DISTANCE;
    }

    public boolean isDuration() {
        return this == DURATION;
    }

    public int weightFrom(Section section) {
        return weightFunction.apply(section);
    }
}
