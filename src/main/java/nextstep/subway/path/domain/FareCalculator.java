package nextstep.subway.path.domain;

import nextstep.subway.path.dto.FarePolicyRequest;

import java.util.List;

public class FareCalculator {
    private final List<FarePolicy> farePolicies;

    private FareCalculator(List<FarePolicy> farePolicies) {
        this.farePolicies = farePolicies;
    }

    public static FareCalculator from(List<FarePolicy> farePolicies) {
        return new FareCalculator(farePolicies);
    }

    public int calculate(FarePolicyRequest request) {
        int fare = 0;
        for (FarePolicy farePolicy : farePolicies) {
            fare += farePolicy.calculate(request);
        }
        return fare;
    }
}
