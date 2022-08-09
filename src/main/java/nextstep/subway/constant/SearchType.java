package nextstep.subway.constant;

import nextstep.subway.domain.Section;

import java.util.function.Function;

public enum SearchType {
    DISTANCE("distance", Section::getDistance),
    DURATION("duration", Section::getDuration)
    ;

    private final String criteria;
    private final Function<Section, Integer> edgeWeight;

    SearchType(String criteria, Function<Section, Integer> edgeWeight) {
        this.criteria = criteria;
        this.edgeWeight = edgeWeight;
    }

    public String getCriteria() {
        return criteria;
    }

    public Function<Section, Integer> getEdgeWeight() {
        return this.edgeWeight;
    }
}
