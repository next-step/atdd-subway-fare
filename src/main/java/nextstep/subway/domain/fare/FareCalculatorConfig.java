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
        var lineSurchargeFareCalculator = new LineSurchargeFareCalculator();

        defaultFareCalculator.setNextChain(firstFareCalculator);
        firstFareCalculator.setNextChain(secondFareCalculator);
        secondFareCalculator.setNextChain(lineSurchargeFareCalculator);

        return defaultFareCalculator;
    }
}
