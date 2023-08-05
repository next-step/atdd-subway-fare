package subway.path.application;

import subway.path.application.dto.PathFareCalculationInfo;

public abstract class PathFareChain {
    private PathFareChain next;

    public static PathFareChain chain(PathFareChain first, PathFareChain... chain) {
        PathFareChain head = first;
        for (PathFareChain nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract PathFareCalculationInfo calculateFare(PathFareCalculationInfo calcInfo);

    protected PathFareCalculationInfo nextCalculateFare(PathFareCalculationInfo calcInfo) {
        if (next == null) {
            return calcInfo;
        }
        return next.calculateFare(calcInfo);
    }

}
