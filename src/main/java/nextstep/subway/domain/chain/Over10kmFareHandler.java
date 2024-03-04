package nextstep.subway.domain.chain;

import static nextstep.subway.domain.chain.BasicFareHandler.BASIC_DISTANCE;

public class Over10kmFareHandler implements FareHandler {
    private static final long START_DISTANCE = 10;
    private static final long END_DISTANCE = 50;
    private static final int OVER_10_FARE_UNIT = 5;

    private FareHandler nextHandler;

    @Override
    public FareHandler setNextHandler(FareHandler fareHandler) {
        this.nextHandler = fareHandler;
        return this;
    }

    @Override
    public long calculate(long distance) {
        long addedFare = addedFare(distance);
        return addedFare + nextCalculate(nextHandler, distance);
    }

    private long addedFare(long distance) {
        if (distance > START_DISTANCE) {
            return calculateFareForDistance(distance);
        }
        return 0L;
    }

    private long calculateFareForDistance(long distance) {
        if (distance <= END_DISTANCE) {
            return calculateOverFare(distance - BASIC_DISTANCE);
        }
        return calculateOverFare(END_DISTANCE - BASIC_DISTANCE);
    }

    private long calculateOverFare(long distance) {
        return (long) ((Math.ceil((distance - 1) / OVER_10_FARE_UNIT) + 1) * 100);
    }

}
