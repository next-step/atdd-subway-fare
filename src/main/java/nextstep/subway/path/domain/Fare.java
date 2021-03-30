package nextstep.subway.path.domain;

import nextstep.subway.path.exception.IllegalFareException;

public class Fare {
    private int fare;

    public Fare(int fare) {
        validateFare(fare);
        this.fare = fare;
    }

    private void validateFare(int fare) {
        if (fare < 0) {
            throw new IllegalFareException();
        }
    }

    public int getFareValue() {
        return fare;
    }
}
