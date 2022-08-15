package nextstep;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.policy.FareManager;
import nextstep.subway.domain.policy.FarePolicy;
import nextstep.subway.domain.policy.discount.DiscountManager;
import nextstep.subway.domain.policy.discount.DiscountPolicy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FareManagerLoader {

    private final List<FarePolicy> farePolicies;
    private final List<DiscountPolicy> discountPolicies;

    @PostConstruct
    public void initialize() {
        initializeFareManager();
        initializeDiscountManager();
    }

    private void initializeFareManager() {
        FareManager.clearPolicy();

        for (FarePolicy farePolicy : farePolicies) {
            FareManager.addPolicy(farePolicy);
        }
    }

    private void initializeDiscountManager() {
        DiscountManager.clearPolicy();

        for (DiscountPolicy discountPolicy : discountPolicies) {
            DiscountManager.addPolicy(discountPolicy);
        }
    }
}
