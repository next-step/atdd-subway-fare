package nextstep.subway.domain;

import java.util.function.Function;

public enum PathFindType {
    DISTANCE("DISTANCE", section -> section.getDistance()),
    DURATION("DURATION", section -> section.getDuration());

    private String value;
    private Function<Section, Integer> findFunction;

    PathFindType(String distance, Function<Section, Integer> findFunction) {
        this.value = distance;
        this.findFunction = findFunction;
    }

    public int find(Section section) {
        return this.findFunction.apply(section);
    }
}
