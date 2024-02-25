package nextstep.path.application;

public class SecondExtraFareHandler extends PathFareHandler {
    private static final long EXTRA_FARE_BASE = 100L;

    @Override
    public long calculateFare(final int distance) {
        final int targetDistance = getTargetDistance(distance);

        int extraFare = 0;
        if (targetDistance > getStandardDistance()) {
            extraFare += calculateOverFare(targetDistance - getStandardDistance());
        }

        return extraFare;
    }

    private int calculateOverFare(final int distance) {
        return (int) ((Math.floor((double) (distance - 1) / getFareInterval()) + 1) * EXTRA_FARE_BASE);
    }

    @Override
    protected int getStandardDistance() {
        return 50;
    }

    @Override
    protected int getFareInterval() {
        return 8;
    }
}
