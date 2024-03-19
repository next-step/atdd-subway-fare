package nextstep.path.fare;

public class FirstExtraFareCalculator extends FareCalculatorHandler {

    private static final int EXTRA_DISTANCE = 50;
    private static final int BASE_DISTANCE = 10;
    private static final int UNIT = 5;
    private static final int ADDITIONAL_FARE = 100;

    @Override
    public int handleFareCalculate(int distance, int fare) {
        if (distance <= EXTRA_DISTANCE) {
            int overFare = calculateOverFare(distance - BASE_DISTANCE);
            return overFare + fare;
        }

        int overFare = calculateOverFare(EXTRA_DISTANCE - BASE_DISTANCE);
        return nextHandler(distance, overFare + fare);
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / UNIT) + 1) * ADDITIONAL_FARE);
    }
}
