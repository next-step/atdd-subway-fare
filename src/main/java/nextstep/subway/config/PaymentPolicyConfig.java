package nextstep.subway.config;

import nextstep.subway.path.application.PaymentPolicyHandler;
import nextstep.subway.path.application.PaymentPolicyHandlerV1;
import nextstep.subway.path.domain.policy.AddedCostPaymentPolicy;
import nextstep.subway.path.domain.policy.AgePaymentPolicy;
import nextstep.subway.path.domain.policy.DistancePaymentPolicy;
import nextstep.subway.path.domain.policy.PaymentPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentPolicyConfig {


    @Bean
    public PaymentPolicyHandler paymentPolicyHandlerV1() {
        PaymentPolicyHandler handler = new PaymentPolicyHandlerV1();
        handler.link(distancePaymentPolicy())
                .link(addedCostPaymentPolicy())
                .link(agePaymentPolicy());

        return handler;
    }

    @Bean
    public PaymentPolicy distancePaymentPolicy() {
        return new DistancePaymentPolicy();
    }

    @Bean
    public PaymentPolicy addedCostPaymentPolicy() {
        return new AddedCostPaymentPolicy();
    }

    @Bean
    public PaymentPolicy agePaymentPolicy() {
        return new AgePaymentPolicy();
    }

}
