package nextstep.path.fare;

public class BaseFareCalculator extends FareCalculatorHandler {

    public static final int BASE_DISTANCE = 10;

    @Override
    public Fare handleFareCalculate(int distance, Fare fare) {
        if (distance <= BASE_DISTANCE) {
            return fare;
        }
        return nextHandler(distance, fare);
    }
}
