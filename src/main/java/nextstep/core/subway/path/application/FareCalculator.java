package nextstep.core.subway.path.application;

import nextstep.core.subway.path.application.dto.PathCompositeWeightEdge;
import nextstep.core.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
