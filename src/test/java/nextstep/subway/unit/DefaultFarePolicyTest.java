package nextstep.subway.unit;

import nextstep.subway.domain.DefaultFarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultFarePolicyTest {
    @DisplayName("기본 정책 요금 확인")
    @Test
    void apply() {
        // given
        int defaultFare = 1250;
        DefaultFarePolicy policy = new DefaultFarePolicy(defaultFare);

        // when & then
        assertThat(policy.apply(0, 1)).isEqualTo(defaultFare);
    }
}
