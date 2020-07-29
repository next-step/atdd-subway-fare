package nextstep.subway.maps.boarding.domain;

import java.util.Arrays;
import java.util.List;

/**
 * {@link FareCalculationPolicy}의 일급 컬렉션
 *
 * @author hyeyoom
 */
public final class FareCalculationPolicyGroup {

    private final List<FareCalculationPolicy> policies;

    public FareCalculationPolicyGroup(FareCalculationPolicy... policies) {
        this.policies = Arrays.asList(policies);
    }

    public int calculateFare(Boarding boarding) {
        final FareCalculationContext context = new FareCalculationContext(boarding);
        for (FareCalculationPolicy policy : policies) {
            policy.calculateFare(context);
        }
        return context.previousResult();
    }
}
