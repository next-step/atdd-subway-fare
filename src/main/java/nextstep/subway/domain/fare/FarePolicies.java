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
        Fare fare = new Fare();
        for (FarePolicy farePolicy : this.farePolicies) {
            fare = farePolicy.addFare(fare, fareBasis);
        }
        return fare.extraTotalFare();
    }
}
