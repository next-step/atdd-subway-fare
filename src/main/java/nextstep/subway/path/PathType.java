package nextstep.subway.path;

import nextstep.subway.path.exception.PathException;

public enum PathType {
    DISTANCE, DURATION;

    public PathFinderStrategy getPathFinderStrategy() {
        switch (this) {
            case DISTANCE:
                return new DistancePathFinderStrategy();
            case DURATION:
                return new DurationPathFinderStrategy();
            default:
                throw new PathException("존재하지 않는 타입입니다.");
        }
    }
}
