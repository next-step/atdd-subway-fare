package nextstep.path.application;

public class BaseFareHandler extends PathFareHandler {
    private static final long BASE_FARE = 1250L;
    private static final int FIRST_STANDARD_DISTANCE = 10;

    @Override
    public long calculate(final int distance) {
        if (distance <= FIRST_STANDARD_DISTANCE) {
            return BASE_FARE;
        }
        return BASE_FARE + calculateNext(distance);
    }

    @Override
    protected int getStandardDistance() {
        return FIRST_STANDARD_DISTANCE;
    }

    @Override
    protected int getStandardInterval() {
        return 0;
    }

}

