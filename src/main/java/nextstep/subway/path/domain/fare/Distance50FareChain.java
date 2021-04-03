package nextstep.subway.path.domain.fare;

public class Distance50FareChain extends DistanceFareChain{

    private static final int SECOND_FARE_THRESHOLD = 50;
    private static final int SECOND_FARE_OFFSET = 8;

    @Override
    public int calculateFare(int distance, int fare) {
        if(distance > SECOND_FARE_THRESHOLD) {
            fare += calculateOverFare(distance - SECOND_FARE_THRESHOLD, SECOND_FARE_OFFSET);
        }
        return fare;
    }
}
