package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.FarePolicy;
import nextstep.subway.path.exception.IllegalFareException;

public class Fare {
    public static final int BASIC_FARE = 1250;

    private int fare;

    public Fare(int fare) {
        validateFare(fare);
        this.fare = fare;
    }

    private void validateFare(int fare) {
        if (fare < BASIC_FARE) {
            throw new IllegalFareException();
        }
    }

    public int getFareValue() {
        return fare;
    }

    public static Fare calculate(FarePolicy... farePolicys) {
        int calculatedFare = 0;
        for (FarePolicy policy : farePolicys) {
            calculatedFare += policy.calculate();
        }
        return new Fare(calculatedFare);
    }
}
