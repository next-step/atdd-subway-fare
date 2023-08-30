package nextstep.subway.path.domain.policy.fare;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FarePolicyConfig {
    @Bean
    public FarePolicy farePolicy() {
        return new LineFarePolicy(new DistanceFarePolicy());
    }
}
