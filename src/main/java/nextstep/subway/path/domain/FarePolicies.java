package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.FarePolicy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FarePolicies {
    private List<FarePolicy> farePolicies;

    private FarePolicies(List<FarePolicy> farePolicies) {
        this.farePolicies = farePolicies;
    }

    public static final FarePolicies of(FarePolicy... policies) {
        return new FarePolicies(addAllFarePolicy(policies));
    }

    private static List<FarePolicy> addAllFarePolicy(FarePolicy... policies) {
        List<FarePolicy> farePolicies = new ArrayList<>();
        farePolicies.addAll(Arrays.asList(policies));
        return farePolicies;
    }

    public int calculate(int fare) {
        int result = fare;
        for (FarePolicy policy : farePolicies) {
            result = policy.fareCalculate(result);
        }
        return result;
    }
}
