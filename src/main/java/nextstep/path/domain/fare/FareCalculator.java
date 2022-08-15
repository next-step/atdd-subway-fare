package nextstep.path.domain.fare;

import java.util.List;

public class FareCalculator {
    private final List<FarePolicy> farePolicies;

    public FareCalculator(List<FarePolicy> farePolicies) {
        this.farePolicies = farePolicies;
    }

    public int calculate() {
        int currentFare = farePolicies.get(0).apply(0);

        for (int i = 1; i < farePolicies.size(); i++) {
            currentFare = farePolicies.get(i).apply(currentFare);
        }

        return currentFare;
    }
}
