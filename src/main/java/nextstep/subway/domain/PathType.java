package nextstep.subway.domain;

import java.util.function.Function;

public enum PathType {
    DISTANCE("최단 거리", SectionEdge::getDistance),
    DURATION("최단 시간", SectionEdge::getDuration);

    private String description;
    private Function<SectionEdge, Integer> expression;

    PathType(String description, Function<SectionEdge, Integer> expression) {
        this.description = description;
        this.expression = expression;
    }

    public int weight(SectionEdge section) {
        return expression.apply(section);
    }
}
