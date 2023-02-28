package nextstep.subway.domain.policy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FarePolicies {
    private final List<FarePolicy> policies;
    private final List<FareDiscountPolicy> discountPolicies;

    public FarePolicies() {

        FarePolicy base = new FixedFarePolicy(1250);
        FarePolicy ten = new BetweenUnitFarePolicy(10, 50, 5, 100);
        FarePolicy fifth = new GreaterUnitFarePolicy(50, 8, 100);
        FarePolicy lineSurcharge = new LineSurchargeFarePolicy();
        policies = Arrays.asList(base, ten, fifth, lineSurcharge);

        FareDiscountPolicy children = new AgeDistcountFarePolicy(6, 13, 0.5F, 350);
        FareDiscountPolicy teen = new AgeDistcountFarePolicy(13, 19, 0.2F, 350);
        discountPolicies = Arrays.asList(children, teen);
    }

    public int calculate(CalculateConditions conditions) {
        int fare = policies.stream().map(policy -> policy.calculate(conditions)).reduce(0, Integer::sum);

        Optional<FareDiscountPolicy> discountPolicy = discountPolicies.stream()
                .filter(policy -> policy.isSuite(conditions))
                .findFirst();

        if (discountPolicy.isPresent()) {
            fare = discountPolicy.get().calculate(fare);
        }

        return fare;
    }
}
