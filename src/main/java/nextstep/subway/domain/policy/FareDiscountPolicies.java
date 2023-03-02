package nextstep.subway.domain.policy;

import nextstep.subway.domain.policy.calculate.CalculateConditions;
import nextstep.subway.domain.policy.discount.AgeDistcountFarePolicy;
import nextstep.subway.domain.policy.discount.FareDiscountPolicy;

import java.util.Arrays;
import java.util.List;

public class FareDiscountPolicies {
    private final List<FareDiscountPolicy> discountPolicies;

    public FareDiscountPolicies() {
        FareDiscountPolicy children = new AgeDistcountFarePolicy(6, 13, 0.5F, 350);
        FareDiscountPolicy teen = new AgeDistcountFarePolicy(13, 19, 0.2F, 350);
        discountPolicies = Arrays.asList(children, teen);
    }

    public int discount(CalculateConditions conditions, int fare) {

        return discountPolicies.stream()
                .filter(policy -> policy.isSuite(conditions))
                .findFirst()
                .map(policy -> policy.discount(fare))
                .orElse(fare);

    }
}
