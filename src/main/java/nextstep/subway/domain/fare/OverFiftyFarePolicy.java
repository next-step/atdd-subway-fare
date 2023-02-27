package nextstep.subway.domain.fare;

public class OverFiftyFarePolicy implements FarePolicy {
    private static final int MIN_DISTANCE = 50;
    private static final int MAX_DISTANCE = 344;
    private static final int EXTRA_FARE_RATE = 8;

    @Override
    public int getFare(final int distance) {
        if (MAX_DISTANCE < distance) {
            throw new InvalidDistanceException();
        }

        return getFare(distance, MIN_DISTANCE, MAX_DISTANCE, EXTRA_FARE_RATE);
    }
}
