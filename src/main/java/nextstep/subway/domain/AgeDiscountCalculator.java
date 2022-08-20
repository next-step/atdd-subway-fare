package nextstep.subway.domain;

public class AgeDiscountCalculator implements FareCalculator {
    private FareCalculator nextCalculator;
    private SubwayFarePolicyType subwayFarePolicyType;

    public AgeDiscountCalculator(SubwayFarePolicyType subwayFarePolicyType){
        this.subwayFarePolicyType = subwayFarePolicyType;
    }

    @Override
    public void nextFareChain(FareCalculator nextCalculator) {
        this.nextCalculator = nextCalculator;
    }

    @Override
    public int calculate(int totalFare, int totalDistance) {
        return (int) ((totalFare - subwayFarePolicyType.getDeductionFare()) * (1.0
                - subwayFarePolicyType.getDiscountPercent()));
    }
}
