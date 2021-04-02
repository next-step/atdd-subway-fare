package nextstep.subway.path.domain.policy;

import java.util.ArrayList;
import java.util.List;

public class Policies {

    private final List<FarePolicy> arrayPolicies = new ArrayList<>();

    public static Policies of(FarePolicy ... policies) {
        return new Policies(policies);
    }

    public Policies(FarePolicy ... policies) {
        for (FarePolicy policy : policies) {
            arrayPolicies.add(policy);
        }
    }

    public int calculate(int fare) {
       int resultFare = fare;
       for (FarePolicy policy : arrayPolicies) {
           resultFare = policy.calculateFare(resultFare);
       }
       return resultFare;
    }
}
