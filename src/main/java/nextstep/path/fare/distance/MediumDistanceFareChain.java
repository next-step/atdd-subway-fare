package nextstep.path.fare.distance;

public class MediumDistanceFareChain implements DistanceFareChain {
    private DistanceFareChain nextFareChain;
    private final int THRESHOLD = 50 - 10;
    private final int BASE_FARE = 100;
    private final int INTERVAL = 5;

    @Override
    public DistanceFareChain addNext(DistanceFareChain nextFareChain) {
        this.nextFareChain = nextFareChain;
        return this;
    }

    @Override
    public int calculate(int distance) {
        if (distance > THRESHOLD) {
            return nextFareChain.calculate(distance);
        }

        return (int) ((Math.ceil((distance - 1) / INTERVAL) + 1) * BASE_FARE);
    }
}
