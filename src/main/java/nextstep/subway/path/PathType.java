package nextstep.subway.path;

import nextstep.subway.path.exception.PathException;
import nextstep.subway.path.strategy.DistancePathFinderStrategy;
import nextstep.subway.path.strategy.DurationPathFinderStrategy;
import nextstep.subway.path.strategy.PathFinderStrategy;

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
