package nextstep.path.fare;

public abstract class FareCalculatorHandler {

    private FareCalculatorHandler nextHandler;

    public FareCalculatorHandler setNextHandler(FareCalculatorHandler fareCalculatorHandler) {
        this.nextHandler = fareCalculatorHandler;
        return fareCalculatorHandler;
    }

    public abstract int handleFareCalculate(int distance, int fare);

    public int nextHandler(int distance, int fare) {
        if (nextHandler != null) {
            return nextHandler.handleFareCalculate(distance, fare);
        }
        return 0;
    }

}
