package nextstep.core.subway.path.application;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FareCalculator {

    private final DistanceFareCalculator distanceFareCalculator;
    private final AdditionalFareCalculator additionalFareCalculator;
    private final AgeFareCalculator ageFareCalculator;

    public FareCalculator(DistanceFareCalculator distanceFareCalculator, AdditionalFareCalculator additionalFareCalculator, AgeFareCalculator ageFareCalculator) {
        this.distanceFareCalculator = distanceFareCalculator;
        this.additionalFareCalculator = additionalFareCalculator;
        this.ageFareCalculator = ageFareCalculator;
    }

    public int calculateTotalFare(int distance, List<Integer> additionalFares) {
        return distanceFareCalculator.calculateDistanceFare(distance) +
                additionalFareCalculator.findMaxAdditionalFare(additionalFares);
    }

    public int calculateTotalFare(int distance, List<Integer> additionalFares, int age) {
        return calculateTotalFare(distance, additionalFares) + ageFareCalculator.calculateAgeDiscount(age);
    }
}
