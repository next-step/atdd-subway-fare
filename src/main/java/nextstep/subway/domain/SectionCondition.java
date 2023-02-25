package nextstep.subway.domain;

import java.util.stream.Stream;

public enum SectionCondition {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration);

    private final PathCondition condition;

    SectionCondition(PathCondition condition) {
        this.condition = condition;
    }

    public static SectionCondition ofType(String type) {
        return Stream.of(values())
                .filter(condition -> condition.name().equalsIgnoreCase(type))
                .findFirst()
                .orElse(DISTANCE);
    }

    public double getWeight(Section section) {
        return condition.getWeight(section);
    }
}
