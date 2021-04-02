package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.FarePolicy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Fare {

    private static final int BASIC_FARE = 1250;

    private int fare;

    private List<FarePolicy> farePolicies = new ArrayList<>();

    public Fare() {
        this.fare = BASIC_FARE;
    }

    public Fare(int fare) {
        this.fare = fare;
    }

    public int getFare() {
        return fare;
    }

    public Fare addAllFarePolicy(FarePolicy... policies) {
        farePolicies.addAll(Arrays.asList(policies));
        return this;
    }

    public int calculate(int fare) {
        int result = fare;
        for (FarePolicy policy : farePolicies) {
            result = policy.fareCalculate(result);
        }
        return result;
    }
}
