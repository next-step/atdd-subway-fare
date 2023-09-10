package nextstep.subway.domain.fare;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FareChainConfig {

    @Bean
    public FareChain defaultFareChain() {
        FareChain kidFareChain = new KidFareChain();
        FareChain teenFareChain = new TeenFareChain();
        FareChain defaultFareChain = new DefaultFareChain();
        FareChain midRangeFareChain = new MidRangeFareChain();
        FareChain longRangeFareChain = new LongRangeFareChain();
        midRangeFareChain.setNextChain(longRangeFareChain);
        defaultFareChain.setNextChain(midRangeFareChain);
        teenFareChain.setNextChain(defaultFareChain);
        kidFareChain.setNextChain(teenFareChain);
        return kidFareChain;
    }
}
