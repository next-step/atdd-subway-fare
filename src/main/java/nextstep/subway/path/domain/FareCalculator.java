package nextstep.subway.path.domain;

import java.util.List;

public class FareCalculator {
    private final List<FarePolicy> farePolicies;

    private FareCalculator(List<FarePolicy> farePolicies) {
        this.farePolicies = farePolicies;
    }

    public static FareCalculator from(List<FarePolicy> farePolicies) {
        return new FareCalculator(farePolicies);
    }

    public Fare calculate() {
        Fare fare = Fare.from(0);
        for (FarePolicy farePolicy : farePolicies) {
            fare = farePolicy.getFare(fare);
        }
        return fare;
    }
}
