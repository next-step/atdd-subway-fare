package nextstep.subway.domain;

import java.util.stream.Stream;

public enum SectionCondition {
    DISTANCE, DURATION;

    public static SectionCondition ofType(String type) {
        return Stream.of(values())
                .filter(condition -> condition.name().equalsIgnoreCase(type))
                .findFirst()
                .orElse(DISTANCE);
    }
}
