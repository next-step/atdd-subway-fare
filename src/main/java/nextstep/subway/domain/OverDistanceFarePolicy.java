package nextstep.subway.domain;

public class OverDistanceFarePolicy {
    private OverFareChain overFareChain;

    private OverDistanceFarePolicy(OverFareChain overFareChain) {
        this.overFareChain = overFareChain;
    }

    public static OverDistanceFarePolicy create() {
        OverFareChain firstChain = new FirstOverFareChain();
        OverFareChain secondChain = new SecondOverFareChain();

        firstChain.setNextChain(secondChain);

        return new OverDistanceFarePolicy(firstChain);
    }

    public Fare calculateOverDistanceFare(int distance) {
        return overFareChain.calculateFare(distance);
    }
}
