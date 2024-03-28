package nextstep.path.fare;

public class Fare {

    private static final int BASE_FARE = 1250;

    public static int calculate(int distance) {
        FareCalculatorHandler fareCalculatorHandler = buildCalculatorChain();
        int fare = fareCalculatorHandler.handleFareCalculate(distance, BASE_FARE);
        return fare;
    }

    private static FareCalculatorHandler buildCalculatorChain() {
        FareCalculatorHandler baseFareCalculator = new BaseFareCalculator();
        baseFareCalculator.setNextHandler(new FirstExtraFareCalculator())
                .setNextHandler(new SecondExtraCalculator());
        return baseFareCalculator;
    }
}
