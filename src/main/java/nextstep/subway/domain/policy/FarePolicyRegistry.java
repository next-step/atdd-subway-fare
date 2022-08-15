package nextstep.subway.domain.policy;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.domain.Path;

public class FarePolicyRegistry {
    private final List<BasicFarePolicy> policies = new ArrayList<>();

    public void addPolicy(BasicFarePolicy policy) {
        policies.add(policy);
    }

    public int calculate(int age, int distance, int fare, Path path) {
        for (BasicFarePolicy policy : policies) {
            fare = policy.calculate(age, fare, distance, path);
        }
        return fare;
    }
}
