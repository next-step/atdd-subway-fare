package nextstep.path.application;

public class PathFareCalculator {

    private final FareChain fareChain;

    public PathFareCalculator() {
        this.fareChain = new FareChain()
                .nextRange(10, 5)
                .nextRange(50, 8);
    }

    public long calculate(final int distance) {
        return fareChain.calculate(distance);
    }
}
