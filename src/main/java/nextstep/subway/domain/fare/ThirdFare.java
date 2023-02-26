package nextstep.subway.domain.fare;

public class ThirdFare implements FareChain {
    private static final int KM = 8;
    private static final int THRESHOLD = 50;

    @Override
    public void setNextChainByChain(FareChain nextFareChain) {
    }

    @Override
    public int calculateFare(int distance) {
        return (int) ((Math.ceil((distance - THRESHOLD - 1) / KM) + 1) * 100);
    }
}
