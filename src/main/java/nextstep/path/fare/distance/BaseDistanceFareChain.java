package nextstep.path.fare.distance;

public class BaseDistanceFareChain implements DistanceFareChain {
    private DistanceFareChain nextFareChain;
    private final int THRESHOLD = 10;
    private final int BASE_FARE = 1250;

    @Override
    public DistanceFareChain addNext(DistanceFareChain nextFareChain) {
        this.nextFareChain = nextFareChain;
        return this;
    }

    @Override
    public int calculate(int distance) {
        if (distance > THRESHOLD) {
            return BASE_FARE
                    + nextFareChain.calculate(distance - THRESHOLD);
        }
        return BASE_FARE;
    }
}
