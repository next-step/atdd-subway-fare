package nextstep.path.fare;

public abstract class FareCalculatorHandler {

    private FareCalculatorHandler nextHandler;

    public FareCalculatorHandler setNextHandler(FareCalculatorHandler fareCalculatorHandler) {
        this.nextHandler = fareCalculatorHandler;
        return fareCalculatorHandler;
    }

    public abstract Fare handleFareCalculate(int distance, Fare fare);

    public Fare nextHandler(int distance, Fare fare) {
        if (nextHandler != null) {
            return nextHandler.handleFareCalculate(distance, fare);
        }
        return Fare.of(0);
    }

}
