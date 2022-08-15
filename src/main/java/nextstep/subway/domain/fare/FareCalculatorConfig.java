package nextstep.subway.domain.fare;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FareCalculatorConfig {

    @Bean
    public FareCalculatorChain fareCalculator() {
        var defaultFareCalculator = new DefaultFareCalculator();
        var firstFareCalculator = new FirstRangeFareCalculator();
        var secondFareCalculator = new SecondRangeFareCalculator();

        defaultFareCalculator.setNextChain(firstFareCalculator);
        firstFareCalculator.setNextChain(secondFareCalculator);

        return defaultFareCalculator;
    }
}
