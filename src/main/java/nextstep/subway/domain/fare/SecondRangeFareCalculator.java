package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;

import java.util.Objects;

public class SecondRangeFareCalculator implements FareCalculatorChain {

    private static final int FARE_PER_UNIT_DISTANCE = 100;
    private static final int RANGE_START = 50;
    private static final float SECOND_UNIT_DISTANCE = 8f;

    private FareCalculatorChain nextChain;

    @Override
    public int calculate(Path path, int initialFare) {
        if (!support(path)) {
            return initialFare;
        }

        var fare = initialFare + getSecondRangeOverFare(getTargetDistance(path.extractDistance()));

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
        return distance - RANGE_START;
    }

    private int getSecondRangeOverFare(int distance) {
        return Math.max(0, (int) Math.ceil(distance / SECOND_UNIT_DISTANCE) * FARE_PER_UNIT_DISTANCE);
    }
}
