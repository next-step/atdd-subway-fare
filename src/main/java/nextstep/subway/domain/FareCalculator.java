package nextstep.subway.domain;

import java.util.Optional;

public class FareCalculator {

    private static final int BASE_FARE = 1250;

    private final FarePolicy farePolicy;

    public FareCalculator(int distance, int fareByLine, Optional<Integer> age) {

        FarePolicy farePolicy1 = new AdditionalFareByDistance(distance);
        FarePolicy farePolicy2 = new AdditionalFareByLine(fareByLine);
        FarePolicy farePolicy3 = new DiscountFareByAge(age);

        farePolicy1.setNext(farePolicy2);
        farePolicy2.setNext(farePolicy3);

        this.farePolicy = farePolicy1;
    }

    public int fare() {
        return farePolicy.fare(BASE_FARE);
    }
}
