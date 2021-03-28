package nextstep.subway.path.domain.policy;

import nextstep.subway.path.domain.Cost;
import nextstep.subway.path.domain.CostByAge;
import nextstep.subway.path.dto.CostRequest;

public class AgePaymentPolicy implements PaymentPolicy {

    @Override
    public CostRequest cost(CostRequest costRequest) {
        Cost cost = CostByAge.applyDiscount(costRequest.getCost(), costRequest.getAge());
        costRequest.updateCost(cost);

        return costRequest;
    }
}
