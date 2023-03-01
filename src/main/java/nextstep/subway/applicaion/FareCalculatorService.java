package nextstep.subway.applicaion;

import nextstep.subway.domain.policy.FareCalculatePolicies;
import nextstep.subway.domain.policy.FareDiscountPolicies;
import nextstep.subway.domain.policy.calculate.CalculateConditions;
import org.springframework.stereotype.Service;

@Service
public class FareCalculatorService {


    private final FareCalculatePolicies fareCalculatePolicies = new FareCalculatePolicies();
    private final FareDiscountPolicies fareDiscountPolicies = new FareDiscountPolicies();

    public int calculateFare(CalculateConditions conditions) {
        int fare = this.fareCalculatePolicies.calculate(conditions);
        return this.fareDiscountPolicies.discount(conditions, fare);
    }
}
