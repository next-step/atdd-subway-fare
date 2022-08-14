package nextstep.subway;

import nextstep.subway.payment.AgeDiscountPaymentPolicy;
import nextstep.subway.payment.DistancePaymentPolicy;
import nextstep.subway.payment.LinePaymentPolicy;
import nextstep.subway.payment.PaymentHandler;
import nextstep.subway.util.discount.AdultPaymentPolicy;
import nextstep.subway.util.discount.AgeFactory;
import nextstep.subway.util.discount.ChildrenPaymentPolicy;
import nextstep.subway.util.discount.TeenagerPaymentPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    @Bean
    public AgeFactory addDiscountAgePolicy() {
        AgeFactory ageFactory = ageFactory();
        ageFactory.addDiscountAgePolicy(new ChildrenPaymentPolicy());
        ageFactory.addDiscountAgePolicy(new TeenagerPaymentPolicy());
        ageFactory.addDiscountAgePolicy(new AdultPaymentPolicy());
        return ageFactory;
    }

    @Bean
    public AgeFactory ageFactory() {
        return new AgeFactory();
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
        paymentHandler.addPaymentPolicy(new AgeDiscountPaymentPolicy(ageFactory()));
        return paymentHandler;
    }
}
