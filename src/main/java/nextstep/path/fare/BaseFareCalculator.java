package nextstep.path.fare;

public class BaseFareCalculator extends FareCalculatorHandler {

    public static final int BASE_DISTANCE = 10;

    @Override
    public int handleFareCalculate(int distance, int fare) {
        if (distance <= BASE_DISTANCE) {
            return fare;
        }
        return nextHandler(distance, fare);
    }
}
