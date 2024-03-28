package nextstep.path.fare;

public class SecondExtraCalculator extends FareCalculatorHandler {

    private static final int BASE_DISTANCE = 50;
    private static final int UNIT = 8;
    private static final int ADDITIONAL_FARE = 100;

    @Override
    public int handleFareCalculate(int distance, int fare) {
        int overFare = calculateOverFare(distance - BASE_DISTANCE);
        return fare + overFare;
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / UNIT) + 1) * ADDITIONAL_FARE);
    }
}
