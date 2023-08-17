package nextstep.subway.constant;

import nextstep.subway.domain.Section;

public enum FindPathType {
    DISTANCE("DISTANCE", Section::getDistance),
    DURATION("DURATION", Section::getDuration);

    private final String type;
    private final EdgeWeightFunction<Section, Integer> edgeWeightFunction;

    FindPathType(String type, EdgeWeightFunction<Section, Integer> edgeWeightFunction) {
        this.type = type;
        this.edgeWeightFunction = edgeWeightFunction;
    }

    public String getType() {
        return type;
    }

    public Integer getEdgeWeight(Section section) {
        return edgeWeightFunction.apply(section);
    }
}
