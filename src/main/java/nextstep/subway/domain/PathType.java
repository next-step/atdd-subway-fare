package nextstep.subway.domain;

import java.util.function.Function;

public enum PathType {
    DISTANCE("거리 기준", Section::getDistance),
    DURATION("시간 기준", Section::getDuration);

    private final String description;
    private final Function<Section, Integer> expression;

    PathType(String description, Function<Section, Integer> expression) {
        this.description = description;
        this.expression = expression;
    }

    public int value(Section section) {
        return expression.apply(section);
    }
}
