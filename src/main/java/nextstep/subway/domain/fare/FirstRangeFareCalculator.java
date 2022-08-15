package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;

public class FirstRangeFareCalculator extends AbstractFareCalculatorChain {

    private static final int FARE_PER_UNIT_DISTANCE = 100;
    private static final float FIRST_UNIT_DISTANCE = 5f;
    private static final int RANGE_START = 10;
    private static final int RANGE_ENDS = 50;

    @Override
    public boolean support(Path path) {
        return path.extractDistance() > RANGE_START;
    }

    @Override
    protected int convert(Path path, int initialFare) {
        int targetDistance = getTargetDistance(path.extractDistance());
        return initialFare + getFirstRangeOverFare(targetDistance);
    }

    private int getTargetDistance(int distance) {
        return Math.min(RANGE_ENDS, distance) - RANGE_START;
    }

    private int getFirstRangeOverFare(int distance) {
        return Math.max(0, (int) Math.ceil(distance / FIRST_UNIT_DISTANCE) * FARE_PER_UNIT_DISTANCE);
    }
}
