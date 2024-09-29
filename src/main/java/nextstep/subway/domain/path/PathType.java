package nextstep.subway.domain.path;

import java.util.Arrays;

public enum PathType {
    DURATION(new LeastTimeFinder()),
    DISTANCE(new LeastDistanceFinder());

    private PathFinder pathFinder;

    PathType(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

    public static PathType from(String type) {
        boolean isTypeUnavailable = Arrays.stream(values())
                .filter(value -> value.name().equals(type))
                .findFirst()
                .isEmpty();
        if (isTypeUnavailable) {
            throw new IllegalArgumentException("요청한 경로 탐색 방법이 유효하지 않습니다.");
        }

        return valueOf(type);
    }

    public PathFinder findPathFinder() {
        return this.pathFinder;
    }
}
