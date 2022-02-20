package nextstep.subway.domain.fare;

public class Over50Fare extends FareChain {

    private static final int OVER_DISTANCE = 50;
    private static final int DISTANCE_PER = 8;
    private static final int OVER_FARE = 100;

    @Override
    public FareChain setNextChain(FareChain nextChain) {
        return super.setNextChain(nextChain);
    }

    @Override
    public boolean isOverFare(int distance) {
        return (distance > OVER_DISTANCE);
    }

    @Override
    public int calculateFare(int distance) {
        return calculateOverFare((distance - OVER_DISTANCE), DISTANCE_PER, OVER_FARE);
    }

}
