package nextstep.subway;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import nextstep.subway.domain.farepolicy.BasicFarePolicy;
import nextstep.subway.domain.farepolicy.DistanceFarePolicy;
import nextstep.subway.domain.farepolicy.DistanceFareRange;
import nextstep.subway.domain.farepolicy.FareCalculator;
import nextstep.subway.domain.farepolicy.FarePolicy;
import nextstep.subway.domain.farepolicy.LineAdditionalFarePolicy;

@Configuration
public class FarePolicyConfig {
    @Bean
    public FareCalculator fareCalculator() {
        return new FareCalculator(Arrays.asList(
            basicFarePolicy(),
            discountMoreThan50(),
            discountUpTo50(),
            lineAdditionalFarePolicy()
        ));
    }

    // 기본 요금 정책
    private FarePolicy basicFarePolicy() {
        return new BasicFarePolicy();
    }

    // 10km 부터 50km 까지의 거리 정책
    private FarePolicy discountMoreThan50() {
        return new DistanceFarePolicy(new DistanceFareRange(10, 50), 5, 100);
    }

    // 50km 초과의 거리 정책
    private FarePolicy discountUpTo50() {
        return new DistanceFarePolicy(new DistanceFareRange(50, Integer.MAX_VALUE), 8, 100);
    }

    // 노선별 추가 요금 정책
    private FarePolicy lineAdditionalFarePolicy() {
        return new LineAdditionalFarePolicy();
    }
}
