package nextstep.path.application;

public class BaseFareHandler extends PathFareHandler {
    private static final long BASE_FARE = 1250L;

    @Override
    public long calculateFare(final int distance) {
        return BASE_FARE;
    }

    @Override
    protected int getStandardDistance() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected int getFareInterval() {
        return 0;
    }

}
