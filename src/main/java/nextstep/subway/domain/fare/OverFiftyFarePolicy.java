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

        if (isNotValidDistance(distance)) {
            return 0;
        }

        int extraDistance = distance - MIN_DISTANCE;
        if (MAX_DISTANCE < distance) {
            extraDistance = MAX_DISTANCE - MIN_DISTANCE;
        }

        return calculateFare(extraDistance, EXTRA_FARE_RATE);
    }

    private boolean isNotValidDistance(final int distance) {
        return distance <= MIN_DISTANCE;
    }
}
