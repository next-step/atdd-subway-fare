package nextstep.subway.path.application;

import nextstep.subway.path.domain.policy.PaymentPolicy;
import nextstep.subway.path.dto.CostRequest;

public interface PaymentPolicyHandler {

    CostRequest execute(CostRequest costRequest);

    PaymentPolicyHandler link(PaymentPolicy paymentPolicy);

}
