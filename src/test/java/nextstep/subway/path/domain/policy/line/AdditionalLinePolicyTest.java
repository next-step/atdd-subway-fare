package nextstep.subway.path.domain.policy.line;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AdditionalLinePolicyTest {

    @Test
    void calculate() {
        // given
        AdditionalLinePolicy policy = new AdditionalLinePolicy(900);

        // when
        int fare = policy.calculate();

        // then
        assertThat(fare).isEqualTo(900);
    }
}
