package nextstep.subway.path.domain;

public class ChainOfFareCalculation {
    private FareCalculationChain c1;

    public ChainOfFareCalculation() {
        this.c1 = new DistanceFareCalculationChain();
        FareCalculationChain c2 = new LineFareCalculationChain();
        FareCalculationChain c3 = new AgeFareCalculationChain();

        c1.setNextChain(c2);
        c2.setNextChain(c3);
    }

    public FareCalculationChain getChain() {
        return c1;
    }
}
