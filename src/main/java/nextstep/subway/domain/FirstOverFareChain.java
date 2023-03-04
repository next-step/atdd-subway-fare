package nextstep.subway.domain;

import static nextstep.subway.domain.Fare.ZERO_FARE;

public class FirstOverFareChain implements OverFareChain {
    private static int SECOND_RANGE_START_KM = 10;
    private static int SECOND_RANGE_END_KM = 50;
    private static int SECOND_RANGE_PER_KM = 5;
    private static int PER_FARE = 100;

    private OverFareChain nextChain;

    @Override
    public void setNextChain(OverFareChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public Fare calculateFare(int distance) {
        Fare fare = ZERO_FARE;
        if (distance > SECOND_RANGE_START_KM) {
            int overDistance = Math.min(distance, SECOND_RANGE_END_KM) - SECOND_RANGE_START_KM;
            fare = calculate(overDistance);
        }
        return fare.plus(nextChain.calculateFare(distance));
    }

    private static Fare calculate(int overDistance) {
        int overFare = (int) (Math.ceil((overDistance + SECOND_RANGE_PER_KM - 1) / SECOND_RANGE_PER_KM) * PER_FARE);
        return Fare.of(overFare);
    }
}
