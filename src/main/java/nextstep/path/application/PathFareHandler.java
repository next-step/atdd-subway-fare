package nextstep.path.application;

import java.util.Objects;

public abstract class PathFareHandler {
    private static final long EXTRA_FARE_BASE = 100L;
    protected PathFareHandler nextHandler;


    public PathFareHandler next(final PathFareHandler nextHandler) {
        if (this.nextHandler == null) {
            this.nextHandler = nextHandler;
        } else {
            this.nextHandler.next(nextHandler);
        }
        return this;
    }


    protected abstract long calculate(int distance);

    protected abstract int getStandardDistance();
    protected abstract int getStandardInterval();

    protected long calculateNext(final int distance) {
        if (Objects.isNull(nextHandler)) {
            return 0;
        }
        return nextHandler.calculate(distance);
    }

    protected int getNextStandardDistance() {
        if (Objects.isNull(nextHandler)) {
            return Integer.MAX_VALUE;
        }
        return nextHandler.getStandardDistance();
    }

    protected int calculateOverFare(final int distance) {
        return (int) ((Math.floor((double) (distance - 1) / getStandardInterval()) + 1) * EXTRA_FARE_BASE);
    }

    protected int getTargetDistance(final int distance) {
        return Math.min(distance, getNextStandardDistance());
    }

}
