package nextstep.subway.domain.fare;


public class BetweenTenAndFiftyFarePolicy implements FarePolicy {
    private static final int MIN_DISTANCE = 10;
    private static final int MAX_DISTANCE = 50;
    private static final int EXTRA_FARE_RATE = 5;

    @Override
    public int getFare(final int distance) {
        return getFare(distance, MIN_DISTANCE, MAX_DISTANCE, EXTRA_FARE_RATE);
    }
}
