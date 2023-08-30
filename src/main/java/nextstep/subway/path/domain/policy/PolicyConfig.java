package nextstep.subway.path.domain.policy;

import nextstep.subway.path.domain.policy.fare.DistanceFarePolicy;
import nextstep.subway.path.domain.policy.fare.FarePolicy;
import nextstep.subway.path.domain.policy.fare.LineFarePolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PolicyConfig {
    @Bean
    public FarePolicy farePolicy() {
        return new LineFarePolicy(new DistanceFarePolicy());
    }
}
