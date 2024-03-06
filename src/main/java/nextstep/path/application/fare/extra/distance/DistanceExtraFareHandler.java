package nextstep.path.application.fare.extra.distance;

import java.util.Objects;

public abstract class DistanceExtraFareHandler {
    protected DistanceExtraFareHandler nextHandler;

    protected void next(final DistanceExtraFareHandler nextHandler) {
        if (Objects.isNull(this.nextHandler)) {
            this.nextHandler = nextHandler;
            return;
        }
        this.nextHandler.next(nextHandler);
    }

    public long calculate(final int distance) {
        return calculateFare(distance) + calculateNext(distance);
    }

    protected abstract long calculateFare(int distance);

    protected abstract int getStandardDistance();

    protected abstract int getFareInterval();

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

    protected int getTargetDistance(final int distance) {
        return Math.min(distance, getNextStandardDistance());
    }

    protected DistanceExtraFareHandler getTail() {
        if (isTail()) {
            return this;
        }
        return nextHandler.getTail();
    }

    private boolean isTail() {
        return Objects.isNull(nextHandler);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final DistanceExtraFareHandler that = (DistanceExtraFareHandler) object;
        return Objects.equals(nextHandler, that.nextHandler);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nextHandler);
    }
}
