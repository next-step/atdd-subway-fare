package nextstep.subway.domain;

import java.util.function.Function;

public enum ShortestType {

    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration)
    ;

    private final Function<Section, Integer> expression;

    ShortestType(Function<Section, Integer> expression) {
        this.expression = expression;
    }

    public int findWeight(Section section) {
        return expression.apply(section);
    }
}
