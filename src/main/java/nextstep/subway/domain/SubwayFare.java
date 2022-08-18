package nextstep.subway.domain;

public class SubwayFare {
    private FareCalculator fareCalculator;
    private static final int BASE_FARE = 1250;
    private static SubwayFarePolicyType subwayFarePolicyType;

    public SubwayFare(SubwayFarePolicyType subwayFarePolicyType) {
        this.subwayFarePolicyType = subwayFarePolicyType;
    }
    public int calculateFare(int extraCharge, int totalDistance) {
        fareCalculator = new FiftiethLessDistanceCalculator();
        FareCalculator longFareCalculator = new FiftiethOverDistanceCalculator();
        FareCalculator ageDiscountCalculator = new AgeDiscountCalculator(subwayFarePolicyType);

        fareCalculator.nextFareChain(longFareCalculator);
        longFareCalculator.nextFareChain(ageDiscountCalculator);

        return fareCalculator.calculate(extraCharge+BASE_FARE, totalDistance);
    }
}
