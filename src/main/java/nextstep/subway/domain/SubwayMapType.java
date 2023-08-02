package nextstep.subway.domain;

import java.util.List;
import java.util.function.Function;

public enum SubwayMapType {
    DURATION(SubwayByDurationMap::new),
    DISTANCE(SubwayByDistanceMap::new);

    private final Function<List<Line>, SubwayMap> expression;

    SubwayMapType(Function<List<Line>, SubwayMap> expression) {
        this.expression = expression;
    }

    public SubwayMap getSubwayMap(List<Line> lines) {
        return expression.apply(lines);
    }
}
