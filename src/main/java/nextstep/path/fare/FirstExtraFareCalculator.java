package nextstep.path.fare;

public class FirstExtraFareCalculator extends FareCalculatorHandler {

    private static final int EXTRA_DISTANCE = 50;
    private static final int BASE_DISTANCE = 10;
    private static final int UNIT = 5;
    private static final int ADDITIONAL_FARE = 100;

    @Override
    public Fare handleFareCalculate(int distance, Fare fare) {
        if (distance <= EXTRA_DISTANCE) {
            Fare overFare = calculateOverFare(distance - BASE_DISTANCE);
            return fare.plus(overFare);
        }

        Fare overFare = calculateOverFare(EXTRA_DISTANCE - BASE_DISTANCE);
        return nextHandler(distance, fare.plus(overFare));
    }

    private Fare calculateOverFare(int distance) {
        int overFare = (int) ((Math.ceil((distance - 1) / UNIT) + 1) * ADDITIONAL_FARE);
        return Fare.of(overFare);
    }
}
