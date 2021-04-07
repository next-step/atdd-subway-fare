package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.fare.policy.FarePolicy;

import java.util.ArrayList;
import java.util.List;

public class FareCalculator {

    private List<FarePolicy> farePolicies = new ArrayList<>();

    public void addPolicy(FarePolicy policy) {
        farePolicies.add(policy);
    }

    public int calculate(int distance) {
        int totalFare = FarePolicy.DEFAULT_FARE;
        for (FarePolicy farePolicy : farePolicies) {
            totalFare = farePolicy.calculate(totalFare, distance);
        }

        return totalFare;
    }
}
