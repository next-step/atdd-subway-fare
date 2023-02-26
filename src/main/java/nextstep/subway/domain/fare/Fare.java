package nextstep.subway.domain.fare;

public class Fare {
    private final FareChain basicFareChain;

    public Fare() {
        this.basicFareChain = new FirstFare();
        FareChain secondFareChain = new SecondFare();
        FareChain thirdFareChain = new ThirdFare();

        basicFareChain.setNextChainByChain(secondFareChain);
        secondFareChain.setNextChainByChain(thirdFareChain);
    }

    public int calculateFare(int distance) {
        return basicFareChain.calculateFare(distance);
    }
}
