package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;

public class SecondRangeFareCalculator extends AbstractFareCalculatorChain {

    private static final int FARE_PER_UNIT_DISTANCE = 100;
    private static final int RANGE_START = 50;
    private static final float SECOND_UNIT_DISTANCE = 8f;

    @Override
    public boolean support(Path path) {
        return path.extractDistance() > RANGE_START;
    }

    @Override
    protected int convert(Path path, int initialFare) {
        int targetDistance = getTargetDistance(path.extractDistance());
        return initialFare + getSecondRangeOverFare(targetDistance);
    }

    private int getTargetDistance(int distance) {
        return distance - RANGE_START;
    }

    private int getSecondRangeOverFare(int distance) {
        return Math.max(0, (int) Math.ceil(distance / SECOND_UNIT_DISTANCE) * FARE_PER_UNIT_DISTANCE);
    }
}
