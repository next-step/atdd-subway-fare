package nextstep.path.application;

public class BaseFareHandler extends PathFareHandler {
    private static final long BASE_FARE = 1250L;

    @Override
    public long calculate(final int distance) {
        return BASE_FARE + calculateNext(distance);
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
