package nextstep.subway.path.domain.fare;

public class Distance10FareChain extends DistanceFareChain{

    private static final int FIRST_FARE_THRESHOLD = 10;
    private static final int FIRST_FARE_OFFSET = 5;
    private static final int FIRST_FARE_LIMIT = 800;

    @Override
    public int calculateFare(int distance, int fare) {
        if(distance > FIRST_FARE_THRESHOLD) {
            fare += Math.min(calculateOverFare(distance - FIRST_FARE_THRESHOLD, FIRST_FARE_OFFSET), FIRST_FARE_LIMIT);
        }
        return fare;
    }
}
