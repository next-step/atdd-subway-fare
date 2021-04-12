package nextstep.subway.path.domain;

import java.time.LocalDateTime;

public class FastPathResult {

    private final PathResult pathResult;
    private final LocalDateTime arriveTime;

    private FastPathResult(PathResult pathResult, LocalDateTime arriveTime) {
        this.pathResult = pathResult;
        this.arriveTime = arriveTime;
    }

    public static FastPathResult of(PathResult pathResult, LocalDateTime arriveTime) {
        return new FastPathResult(pathResult, arriveTime);
    }

    public PathResult getPathResult() {
        return pathResult;
    }

    public LocalDateTime getArriveTime() {
        return arriveTime;
    }
}
