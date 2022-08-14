package nextstep.subway;

import nextstep.subway.payment.AgeDiscountPaymentPolicy;
import nextstep.subway.payment.DistancePaymentPolicy;
import nextstep.subway.payment.LinePaymentPolicy;
import nextstep.subway.payment.PaymentHandler;
import nextstep.subway.util.discount.AdultPaymentPolicy;
import nextstep.subway.util.discount.DiscountAgePolicyFactory;
import nextstep.subway.util.discount.ChildrenPaymentPolicy;
import nextstep.subway.util.discount.TeenagerPaymentPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    @Bean
    public DiscountAgePolicyFactory addDiscountAgePolicy() {
        DiscountAgePolicyFactory discountAgePolicyFactory = discountAgePolicyFactory();
        discountAgePolicyFactory.addDiscountAgePolicy(new ChildrenPaymentPolicy());
        discountAgePolicyFactory.addDiscountAgePolicy(new TeenagerPaymentPolicy());
        discountAgePolicyFactory.addDiscountAgePolicy(new AdultPaymentPolicy());
        return discountAgePolicyFactory;
    }

    @Bean
    public DiscountAgePolicyFactory discountAgePolicyFactory() {
        return new DiscountAgePolicyFactory();
    }

    @Bean
    public PaymentHandler paymentHandler() {
        return new PaymentHandler();
    }

    @Bean
    public PaymentHandler addPaymentHandler() {
        PaymentHandler paymentHandler = paymentHandler();
        paymentHandler.addPaymentPolicy(new DistancePaymentPolicy());
        paymentHandler.addPaymentPolicy(new LinePaymentPolicy());
        paymentHandler.addPaymentPolicy(new AgeDiscountPaymentPolicy(discountAgePolicyFactory()));
        return paymentHandler;
    }
}
