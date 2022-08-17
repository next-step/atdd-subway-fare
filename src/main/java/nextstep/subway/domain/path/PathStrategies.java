package nextstep.subway.domain.path;

import nextstep.subway.domain.Section;

import java.util.Arrays;
import java.util.function.Function;

public enum PathStrategies {

    DISTANCE("DISTANCE", section -> section.getDistance()),
    DURATION("DURATION",section -> section.getDuration());

    private String type;
    private Function<Section, Integer> function;

    PathStrategies(String type, Function<Section, Integer> function) {
        this.type = type;
        this.function = function;
    }

    public Integer apply(Section section) {
        return function.apply(section);
    }

    public static PathStrategies find(String type) {
        return Arrays.stream(PathStrategies.values())
                .filter(p -> p.type.equals(type))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }


}
