package nextstep.subway.domain;

import nextstep.subway.applicaion.edge.DistanceEdgeInitiator;
import nextstep.subway.applicaion.edge.DurationEdgeInitiator;
import nextstep.subway.applicaion.edge.EdgeInitiator;

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
