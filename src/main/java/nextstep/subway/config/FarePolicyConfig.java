package nextstep.subway.config;

import nextstep.subway.applicaion.FarePolicyHandler;
import nextstep.subway.applicaion.FarePolicyHandlerV1;
import nextstep.subway.domain.AgeFarePolicy;
import nextstep.subway.domain.DistanceFarePolicy;
import nextstep.subway.domain.LineAddFarePolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FarePolicyConfig {

    @Bean
    public FarePolicyHandler farePolicyHandlerV1() {
        FarePolicyHandler handler = new FarePolicyHandlerV1();
        return handler.chain(new DistanceFarePolicy())
                .chain(new LineAddFarePolicy())
                .chain(new AgeFarePolicy());
    }
}
