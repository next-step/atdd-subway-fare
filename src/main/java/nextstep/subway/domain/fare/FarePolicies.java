package nextstep.subway.domain.fare;

import java.util.ArrayList;
import java.util.List;

public class FarePolicies {
    private final List<FarePolicy> farePolicies = new ArrayList<>();

    public FarePolicies() {
        this.farePolicies.add(DistancePolicy.getInstance());
        this.farePolicies.add(LinePolicy.getInstance());
        this.farePolicies.add(AgePolicy.getInstance());
    }

    public int calculateFare(FareBasis fareBasis) {
        Fare fare = new Fare();
        for (FarePolicy farePolicy : this.farePolicies) {
            fare = farePolicy.addFare(fare, fareBasis);
        }
        return fare.calculateTotalFare();
    }
}
