package nextstep.subway.ui;

import nextstep.subway.domain.DistancePathType;
import nextstep.subway.domain.DurationPathType;
import nextstep.subway.domain.PathType;

public enum PathTypeParam {
    DISTANCE(new DistancePathType()),
    DURATION(new DurationPathType());

    private PathType pathType;

    PathTypeParam(PathType pathType) {
        this.pathType = pathType;
    }

    public PathType toDomain() {
        return this.pathType;
    }
}
