package nextstep.subway.domain;

import nextstep.subway.domain.graph.DistanceEdgeInitiator;
import nextstep.subway.domain.graph.DurationEdgeInitiator;
import nextstep.subway.domain.graph.EdgeInitiator;

public enum PathType {
    DISTANCE(new DistanceEdgeInitiator()),
    DURATION(new DurationEdgeInitiator());

    private EdgeInitiator edgeInitiator;

    PathType(EdgeInitiator edgeInitiator) {
        this.edgeInitiator = edgeInitiator;
    }

    public EdgeInitiator getEdgeInitiator() {
        return edgeInitiator;
    }
}
