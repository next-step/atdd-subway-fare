package nextstep.subway.path.domain.policy.distance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultDistancePolicyTest {

    private final int BASIC_FARE = 1250;

    @DisplayName("기본 요금 계산")
    @Test
    void calculate() {
        // given
        DefaultDistancePolicy policy = new DefaultDistancePolicy();

        // when
        int fare = policy.calculateFare(BASIC_FARE);

        // then
        assertThat(fare).isEqualTo(BASIC_FARE);
    }
}
