package nextstep.subway.domain;

import java.util.function.Function;

public enum PathType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration),
    ;

    public double weight(final Section section) {
        return expression.apply(section);
    }

    private final Function<Section, Integer> expression;

    PathType(final Function<Section, Integer> expression) {
        this.expression = expression;
    }
}
