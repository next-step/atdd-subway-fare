package nextstep.subway.path.domain.policy.line;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdditionalLinePolicyTest {

    @Test
    void calculate() {
        // given
        AdditionalLinePolicy policy = new AdditionalLinePolicy(900);

        // when
        int fare = policy.calculateFare(200);

        // then
        assertThat(fare).isEqualTo(1100);
    }
}
