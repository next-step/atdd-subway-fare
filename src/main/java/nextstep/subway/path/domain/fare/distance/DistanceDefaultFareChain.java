package nextstep.subway.path.domain.fare.distance;

public class DistanceDefaultFareChain extends DistanceFareChain{

    private static final int DEFAULT_FARE = 1250;

    @Override
    protected int calculateFare(int distance) {
        return DEFAULT_FARE;
    }
}
