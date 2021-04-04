package nextstep.subway.path.domain.fare.distance;

public class Distance10FareChain extends DistanceFareChain{

    private static final int FIRST_FARE_DEFAULT = 0;
    private static final int FIRST_FARE_THRESHOLD = 10;
    private static final int FIRST_FARE_OFFSET = 5;
    private static final int FIRST_FARE_LIMIT = 800;

    @Override
    protected int calculateFare(int distance) {
        if(distance <= FIRST_FARE_THRESHOLD) {
            return FIRST_FARE_DEFAULT;
        }

        return Math.min(calculateOverFare(distance - FIRST_FARE_THRESHOLD, FIRST_FARE_OFFSET), FIRST_FARE_LIMIT);
    }
}
