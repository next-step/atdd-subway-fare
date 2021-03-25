package nextstep.subway.line.domain;

import java.util.function.Function;

public enum PathType {
    DISTANCE(Section::getDistance),

    DURATION(Section::getDuration);

    private Function<Section, Integer> expression;

    PathType(Function<Section, Integer> expression) {
        this.expression = expression;
    }

    public int findWeightOf(Section section) {
        return expression.apply(section);
    }
}
