package nextstep.subway.domain.fare;

import java.util.ArrayList;
import java.util.List;

public class FarePolicies {
    private final List<FarePolicy> farePolicies = new ArrayList<>();

    public FarePolicies() {
        farePolicies.addAll(List.of(
                DistancePolicy.getInstance(),
                LinePolicy.getInstance(),
                AgePolicy.getInstance()
        ));
    }

    public int calculateFare(FareBasis fareBasis) {
        return this.farePolicies.stream()
                .reduce(new Fare(),
                        (fare, farePolicy) -> farePolicy.calculateFare(fare, fareBasis),
                        (fare1, fare2) -> fare1)
                .extraTotalFare();
    }
}
