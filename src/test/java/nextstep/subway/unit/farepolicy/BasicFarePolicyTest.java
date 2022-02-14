package nextstep.subway.unit.farepolicy;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.domain.farepolicy.BasicFarePolicy;
import nextstep.subway.domain.farepolicy.DistanceFarePolicy;
import nextstep.subway.domain.farepolicy.FarePolicy;

@DisplayName("기본 요금 정책 테스트")
public class BasicFarePolicyTest {
    @ValueSource(ints = { 10, 100, 1000 })
    @DisplayName("무조건 1250원의 요금이 부과 된다.")
    @ParameterizedTest
    void calculate(int distance) {
        FarePolicy farePolicy = new BasicFarePolicy();

        assertThat(farePolicy.calculate(distance)).isEqualTo(1250);
    }
}
