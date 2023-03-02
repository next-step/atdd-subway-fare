package nextstep.subway.domain.policy;

import nextstep.subway.domain.policy.calculate.*;

import java.util.Arrays;
import java.util.List;

public class FareCalculatePolicies {
    private final List<FareCalculatePolicy> policies;

    public FareCalculatePolicies() {

        FareCalculatePolicy base = new FixedFarePolicy(1250);
        FareCalculatePolicy ten = new BetweenUnitFarePolicy(10, 50, 5, 100);
        FareCalculatePolicy fifth = new GreaterUnitFarePolicy(50, 8, 100);
        FareCalculatePolicy lineSurcharge = new LineSurchargeFarePolicy();
        policies = Arrays.asList(base, ten, fifth, lineSurcharge);

    }

    public int calculate(CalculateConditions conditions) {
        return policies.stream().map(policy -> policy.calculate(conditions)).reduce(0, Integer::sum);
    }
}
