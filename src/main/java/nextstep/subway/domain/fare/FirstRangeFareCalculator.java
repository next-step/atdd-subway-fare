package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;

import java.util.Objects;

public class FirstRangeFareCalculator implements FareCalculatorChain {

    private static final int FARE_PER_UNIT_DISTANCE = 100;
    private static final float FIRST_UNIT_DISTANCE = 5f;
    private static final int RANGE_START = 10;
    private static final int RANGE_ENDS = 50;

    private FareCalculatorChain nextChain;

    @Override
    public int calculate(Path path, int initialFare) {
        if (!support(path)) {
            return initialFare;
        }

        var targetDistance = getTargetDistance(path.extractDistance());
        var fare =  initialFare + getFirstRangeOverFare(targetDistance);

        if (Objects.isNull(nextChain)) {
            return fare;
        }

        return nextChain.calculate(path, fare);
    }

    @Override
    public boolean support(Path path) {
        return path.extractDistance() > RANGE_START;
    }

    @Override
    public void setNextChain(FareCalculatorChain chain) {
        this.nextChain = chain;
    }

    private int getTargetDistance(int distance) {
        return Math.min(RANGE_ENDS, distance) - RANGE_START;
    }

    private int getFirstRangeOverFare(int distance) {
        return Math.max(0, (int) Math.ceil(distance / FIRST_UNIT_DISTANCE) * FARE_PER_UNIT_DISTANCE);
    }
}
