package nextstep.subway;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import nextstep.subway.domain.farepolicy.BasicFarePolicy;
import nextstep.subway.domain.farepolicy.FareCalculator;
import nextstep.subway.domain.farepolicy.LineAdditionalFarePolicy;
import nextstep.subway.domain.farepolicy.MoreThan50FarePolicy;
import nextstep.subway.domain.farepolicy.UpTo50FarePolicy;

@Configuration
public class FarePolicyConfig {
    @Bean
    public FareCalculator fareCalculator() {
        return new FareCalculator(Arrays.asList(
            new BasicFarePolicy(),          /* 기본 요금 정책 */
            new MoreThan50FarePolicy(),     /* 10km 부터 50km 까지의 거리 정책 */
            new UpTo50FarePolicy(),         /* 50km 초과의 거리 정책 */
            new LineAdditionalFarePolicy()  /* 노선별 추가 요금 정책 */
        ));
    }
}
