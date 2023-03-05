package nextstep.subway.domain;

import static nextstep.subway.domain.Fare.ZERO_FARE;

public class SecondOverFareChain implements OverFareChain {
    private static int THIRD_RANGE_START_KM = 50;
    private static int THIRD_RANGE_PER_KM = 8;
    private static int PER_FARE = 100;

    private OverFareChain nextChain;

    @Override
    public void setNextChain(OverFareChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public Fare calculateFare(int distance) {
        if (distance <= THIRD_RANGE_START_KM) {
            return ZERO_FARE;
        }

        int overDistance = distance - THIRD_RANGE_START_KM;
        return calculate(overDistance);
    }

    private static Fare calculate(int overDistance) {
        int overFare = (int) (Math.ceil((overDistance + THIRD_RANGE_PER_KM - 1) / THIRD_RANGE_PER_KM) * PER_FARE);
        return Fare.of(overFare);
    }
}
