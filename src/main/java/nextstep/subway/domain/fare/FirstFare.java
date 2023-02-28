package nextstep.subway.domain.fare;

public class FirstFare implements FareChain{
    private final int THRESHOLD = 10;
    FareChain nextFareChain;

    @Override
    public void setNextChainByChain(FareChain nextFareChain) {
        this.nextFareChain = nextFareChain;
    }

    @Override
    public int calculateFare(int distance) {
        if (distance > THRESHOLD) {
            return calculateFare(THRESHOLD)
                    + nextFareChain.calculateFare(distance);
        }
        return 1250;
    }
}
