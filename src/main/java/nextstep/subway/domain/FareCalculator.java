package nextstep.subway.domain;

import org.springframework.lang.Nullable;

import java.util.Optional;

public class FareCalculator {

    private static final int BASE_FARE = 1250;

    private final AdditionalFareByDistance additionalFareByDistance;
    private final AdditionalFareByLine additionalFareByLine;
    private final DiscountFareByAge discountFareByAge;

    public FareCalculator(int distance, int fareByLine, Optional<Integer> age) {
        this.additionalFareByDistance = new AdditionalFareByDistance(distance);
        this.additionalFareByLine = new AdditionalFareByLine(fareByLine);
        this.discountFareByAge = new DiscountFareByAge(age);
    }

    public int fare() {

        int fare = BASE_FARE;
        fare = additionalFareByDistance.fare(fare);
        fare = additionalFareByLine.fare(fare);
        fare = discountFareByAge.fare(fare);

        return fare;
    }
}
