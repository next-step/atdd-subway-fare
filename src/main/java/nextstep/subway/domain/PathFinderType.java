package nextstep.subway.domain;

import com.google.common.base.Function;

public enum PathFinderType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration);

    private Function<Section, Integer> func;

    PathFinderType(Function<Section, Integer> func) {
        this.func = func;
    }

    public int getEdgeWeight(Section section) {
        return func.apply(section);
    }
}
