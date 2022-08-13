package nextstep.subway;

import nextstep.subway.util.discount.Adult;
import nextstep.subway.util.discount.AgeFactory;
import nextstep.subway.util.discount.Children;
import nextstep.subway.util.discount.Teenager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    @Bean
    public void addDiscountAgePolicy() {
        AgeFactory ageFactory = ageFactory();
        ageFactory.addDiscountAgePolicy(new Children());
        ageFactory.addDiscountAgePolicy(new Teenager());
        ageFactory.addDiscountAgePolicy(new Adult());
    }

    @Bean
    public AgeFactory ageFactory() {
        return new AgeFactory();
    }
}
