package nextstep.subway.constant;

import nextstep.subway.domain.Section;

import java.util.function.Function;

public enum SearchType {
    DISTANCE("distance", Section::getDistance),
    DURATION("duration", Section::getDuration)
    ;

    private final String criteria;
    private final Function<Section, Integer> getEdgeWeight;

    SearchType(String criteria, Function<Section, Integer> getEdgeWeight) {
        this.criteria = criteria;
        this.getEdgeWeight = getEdgeWeight;
    }

    public String getCriteria() {
        return criteria;
    }

    public Function<Section, Integer> getGetEdgeWeight() {
        return this.getEdgeWeight;
    }
}
