package nextstep.subway.path.domain.policy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OverFiftyDistancePolicyTest {

    @DisplayName("거리 50킬로 넘을 때 요금 계산")
    @Test
    void calculate() {
        // given
        OverFiftyDistancePolicy policy = new OverFiftyDistancePolicy(53);

        // when
        int fare = policy.calculate();

        // then
        assertThat(fare).isEqualTo(2150);
    }
}
