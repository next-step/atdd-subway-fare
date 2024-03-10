package nextstep.core.subway.pathFinder.application;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FareCalculator {

    private final DistanceFareCalculator distanceFareCalculator;
    private final AdditionalFareCalculator additionalFareCalculator;

    public FareCalculator(DistanceFareCalculator distanceFareCalculator, AdditionalFareCalculator additionalFareCalculator) {
        this.distanceFareCalculator = distanceFareCalculator;
        this.additionalFareCalculator = additionalFareCalculator;
    }

    public int calculateTotalFare(int distance, List<Integer> additionalFares) {
        return distanceFareCalculator.calculateDistanceFare(distance) +
                additionalFareCalculator.findMaxAdditionalFare(additionalFares);
    }
}
