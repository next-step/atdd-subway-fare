package nextstep.subway.domain;

import java.util.HashMap;
import java.util.Map;

public enum SectionCondition {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration);

    private static final Map<String, SectionCondition> conditionMap;

    private final PathCondition condition;

    static {
        conditionMap = new HashMap<>();
        conditionMap.put("distance", DISTANCE);
        conditionMap.put("duration", DURATION);
    }

    SectionCondition(PathCondition condition) {
        this.condition = condition;
    }

    public static SectionCondition ofType(String type) {
        return conditionMap.getOrDefault(type.toLowerCase(), DISTANCE);
    }

    public double getWeight(Section section) {
        return condition.getWeight(section);
    }
}
