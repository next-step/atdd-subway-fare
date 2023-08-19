package nextstep.subway.domain.fare;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FareChainCalculator {

    @Bean
    public FareChain defaultFareChain() {
        FareChain defaultFareChain = new DefaultFareChain();
        FareChain midRangeFareChain = new MidRangeFareChain();
        FareChain longRangeFareChain = new LongRangeFareChain();
        midRangeFareChain.setNextChain(longRangeFareChain);
        defaultFareChain.setNextChain(midRangeFareChain);
        return defaultFareChain;
    }
}
