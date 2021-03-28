package nextstep.subway.path.application;

import nextstep.subway.path.domain.policy.PaymentPolicy;
import nextstep.subway.path.dto.CostRequest;

import java.util.ArrayList;
import java.util.List;

public class PaymentPolicyHandlerV1 implements PaymentPolicyHandler{
    private List<PaymentPolicy> policies = new ArrayList<>();

    @Override
    public CostRequest execute(CostRequest costRequest) {
        for (PaymentPolicy policy : policies) {
            costRequest = policy.cost(costRequest);
        }
        return costRequest;
    }

    @Override
    public PaymentPolicyHandler link(PaymentPolicy paymentPolicy) {
        policies.add(paymentPolicy);
        return this;
    }
}
