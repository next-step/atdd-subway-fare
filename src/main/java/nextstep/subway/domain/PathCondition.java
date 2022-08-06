package nextstep.subway.domain;

import nextstep.subway.domain.graph.DistanceEdgeInitiator;
import nextstep.subway.domain.graph.DurationEdgeInitiator;
import nextstep.subway.domain.graph.EdgeInitiator;

public enum PathCondition {
    DISTANCE(new DistanceEdgeInitiator()),
    DURATION(new DurationEdgeInitiator());

    private EdgeInitiator edgeInitiator;

    PathCondition(EdgeInitiator edgeInitiator) {
        this.edgeInitiator = edgeInitiator;
    }

    public EdgeInitiator getEdgeInitiator() {
        return edgeInitiator;
    }
}
