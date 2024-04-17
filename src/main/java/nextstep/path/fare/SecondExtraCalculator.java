package nextstep.path.fare;

public class SecondExtraCalculator extends FareCalculatorHandler {

    private static final int BASE_DISTANCE = 50;
    private static final int UNIT = 8;
    private static final int ADDITIONAL_FARE = 100;

    @Override
    public Fare handleFareCalculate(int distance, Fare fare) {
        Fare overFare = calculateOverFare(distance - BASE_DISTANCE);
        return fare.plus(overFare);
    }

    private Fare calculateOverFare(int distance) {
        int overFare = (int) ((Math.ceil((distance - 1) / UNIT) + 1) * ADDITIONAL_FARE);
        return Fare.of(overFare);
    }
}
