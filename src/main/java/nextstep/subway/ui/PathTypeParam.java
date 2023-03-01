package nextstep.subway.ui;

import nextstep.subway.domain.*;

public enum PathTypeParam {
    DISTANCE, DURATION;

    public PathType toDomain() {
        if (this == DISTANCE) {
            return new DistancePathType();
        }
        
        if (this == DURATION) {
            return new DurationPathType();
        }
        
        throw new IllegalArgumentException("변경할 수 있는 PathTye이 없음");
    }
}
