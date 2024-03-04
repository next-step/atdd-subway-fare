package nextstep.subway.domain.chain;

public class Over50kmFareHandler implements FareHandler {
    private static final long START_DISTANCE = 50;
    private static final int OVER_50_FARE_UNIT = 8;

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
        return calculateOverFare(distance - START_DISTANCE);
    }

    private long calculateOverFare(long distance) {
        return (long) ((Math.ceil((distance - 1) / OVER_50_FARE_UNIT) + 1) * 100);
    }
}
