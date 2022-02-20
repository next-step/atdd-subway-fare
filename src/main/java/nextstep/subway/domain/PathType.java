package nextstep.subway.domain;

import java.util.function.Function;

public enum PathType {
    DISTANCE("최단 거리", Section::getDistance),
    DURATION("최단 시간", Section::getDuration);

    private String description;
    private Function<Section, Integer> expression;

    PathType(String description, Function<Section, Integer> expression) {
        this.description = description;
        this.expression = expression;
    }

    public int weight(Section section) {
        return expression.apply(section);
    }
}
