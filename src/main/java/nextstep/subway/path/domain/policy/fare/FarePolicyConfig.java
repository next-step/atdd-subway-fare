package nextstep.subway.path.domain.policy.fare;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FarePolicyConfig {
    @Bean
    public FarePolicy farePolicy(List<DistanceFareRule> distanceFareRules) {
        return new LineFarePolicy(new DistanceFarePolicy(distanceFareRules));
    }

}
