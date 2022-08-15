package nextstep.subway.domain.fare;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FareCalculatorConfig {

    @Bean
    public FareCalculatorChain fareCalculator() {
        FareCalculatorChain defaultFare = new DefaultFareCalculator();
        FareCalculatorChain firstRangeFare = new FirstRangeFareCalculator();
        FareCalculatorChain secondRangeFare = new SecondRangeFareCalculator();
        FareCalculatorChain lineSurchargeFare = new LineSurchargeFareCalculator();

        defaultFare.setNextChain(firstRangeFare);
        firstRangeFare.setNextChain(secondRangeFare);
        secondRangeFare.setNextChain(lineSurchargeFare);

        return defaultFare;
    }
}
