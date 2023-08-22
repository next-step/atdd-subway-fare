package nextstep.subway.domain.fare;

public class DefaultFareChain implements FareChain{

    private static final int DEFAULT_FARE = 1250;
    private static final int BASIC_DISTANCE = 10;
    private FareChain midRangeFare;

    @Override
    public void setNextChain(FareChain fareChain) {
        this.midRangeFare = fareChain;
    }

    @Override
    public int calculateFare(int distance) {
        if (distance > BASIC_DISTANCE) {
            return DEFAULT_FARE + midRangeFare.calculateFare(distance);
        }
        return DEFAULT_FARE;
    }
}
