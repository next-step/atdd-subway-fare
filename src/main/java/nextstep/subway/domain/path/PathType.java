package nextstep.subway.domain.path;

import nextstep.subway.domain.path.fee.CalculateHandler;

public enum PathType {
    DURATION {
        @Override
        public PathFinder createPathFinder(CalculateHandler handler) {
            return new ShortestDurationPathFinder(handler);
        }
    }, DISTANCE {
        @Override
        public PathFinder createPathFinder(CalculateHandler handler) {
            return new ShortestDistancePathFinder(handler);
        }
    };

    public abstract PathFinder createPathFinder(CalculateHandler handler);
}
