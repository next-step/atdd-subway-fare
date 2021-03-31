package nextstep.subway.path.domain.policy.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultLinePolicyTest {

    @DisplayName("기본 요금 계산")
    @Test
    void calculate() {
        // given
        DefaultLinePolicy policy = new DefaultLinePolicy();

        // when
        int fare = policy.calculate();

        // then
        assertThat(fare).isEqualTo(0);
    }
}