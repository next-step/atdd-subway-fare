package nextstep.core.subway.path.application;

import nextstep.core.auth.domain.constants.AgeGroup;
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

    public int calculateTotalFare(int distance, List<Integer> additionalFares, AgeGroup ageGroup) {
        return distanceFareCalculator.calculateDistanceFare(distance) +
                additionalFareCalculator.findMaxAdditionalFare(additionalFares) +
                ageFareCalculator.calculateAgeDiscount(ageGroup);
    }
}
