package nextstep.subway.domain.policy;

import java.util.ArrayList;
import java.util.List;

public class FarePolicies {
    List<FarePolicy> values;

    public FarePolicies() {
        this.values = new ArrayList<>();
    }

    public void addPolicies(FarePolicy... policies) {
        this.values.addAll(List.of(policies));
    }

    public int calculate(int distance) {
        return values.stream().map(policy -> policy.calculate(distance)).reduce(0, Integer::sum);
    }
}
