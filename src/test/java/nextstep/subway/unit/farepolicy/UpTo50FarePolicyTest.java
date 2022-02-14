package nextstep.subway.unit.farepolicy;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.domain.farepolicy.BasicFarePolicy;
import nextstep.subway.domain.farepolicy.FarePolicy;
import nextstep.subway.domain.farepolicy.UpTo50FarePolicy;

@DisplayName("50km 까지의 요금 정책 테스트")
public class UpTo50FarePolicyTest {
    @CsvSource({
        "12,100",
        "16,200",
        "21,300"
    })
    @DisplayName("50km 까지는 10km를 초과하는 거리에 대해 5km당 100원의 요금이 부과 된다.")
    @ParameterizedTest
    void calculate(int distance, int cost) {
        FarePolicy farePolicy = new UpTo50FarePolicy();

        assertThat(farePolicy.calculate(distance)).isEqualTo(cost);
    }
}
