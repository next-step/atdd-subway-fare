package nextstep.subway.domain.fare;

public class SecondFare implements FareChain {
    private static final int KM = 5;
    private static final int THRESHOLD = 50;
    private static final int BASIC_THRESHOLD = 10;

    private FareChain nextFareChain;

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
        return (int) ((Math.ceil((distance - BASIC_THRESHOLD - 1) / KM) + 1) * 100);
    }
}
