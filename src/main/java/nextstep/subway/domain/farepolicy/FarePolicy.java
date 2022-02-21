package nextstep.subway.domain.farepolicy;

import java.util.LinkedHashSet;
import java.util.Set;

public class FarePolicy {

    protected static final int DEFAULT_FARE = 1250;

    private Set<Policy> policySet = new LinkedHashSet<>();

    public FarePolicy appendPolicy(Policy farePolicy) {
        policySet.add(farePolicy);
        return this;
    }

    public int calculateFare() {
        int fare = DEFAULT_FARE;

        for (Policy policy : policySet) {
            fare = policy.calculate(fare);
        }

        return fare;
    }
}
