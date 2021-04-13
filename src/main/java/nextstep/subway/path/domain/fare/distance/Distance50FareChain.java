package nextstep.subway.path.domain.fare.distance;

public class Distance50FareChain extends DistanceFareChain{

    private static final int SECOND_FARE_DEFAULT = 0;
    private static final int SECOND_FARE_THRESHOLD = 50;
    private static final int SECOND_FARE_OFFSET = 8;

    @Override
    protected int calculateFare(int distance) {
        if(distance <= SECOND_FARE_THRESHOLD) {
            return SECOND_FARE_DEFAULT;
        }

        return calculateOverFare(distance - SECOND_FARE_THRESHOLD, SECOND_FARE_OFFSET);
    }
}
