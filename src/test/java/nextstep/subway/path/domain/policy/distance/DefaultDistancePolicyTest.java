package nextstep.subway.path.domain.policy.distance;

import nextstep.subway.path.domain.policy.distance.DefaultDistancePolicy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultDistancePolicyTest {

    private final int BASIC_FARE = 1250;

    @DisplayName("기본 요금 계산")
    @Test
    void calculate() {
        // given
        DefaultDistancePolicy policy = new DefaultDistancePolicy();

        // when
        int fare = policy.calculate();

        // then
        Assertions.assertThat(fare).isEqualTo(BASIC_FARE);
    }
}
