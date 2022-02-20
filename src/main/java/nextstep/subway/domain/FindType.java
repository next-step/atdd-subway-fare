package nextstep.subway.domain;

import java.util.Arrays;

public enum FindType {
    DISTANCE, DURATION;

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
}
