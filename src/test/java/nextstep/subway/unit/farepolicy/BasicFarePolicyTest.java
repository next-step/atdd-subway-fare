package nextstep.subway.unit.farepolicy;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.domain.farepolicy.BasicFarePolicy;
import nextstep.subway.domain.farepolicy.FarePolicy;

@DisplayName("기본 요금 정책 테스트")
public class BasicFarePolicyTest {
    @DisplayName("무조건 1250원의 요금이 부과 된다.")
    @Test
    void calculate() {
        FarePolicy farePolicy = new BasicFarePolicy();

        assertThat(farePolicy.calculate()).isEqualTo(1250);
    }
}
