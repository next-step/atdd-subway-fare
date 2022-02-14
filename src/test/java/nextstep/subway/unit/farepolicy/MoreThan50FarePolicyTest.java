package nextstep.subway.unit.farepolicy;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.domain.farepolicy.FarePolicy;
import nextstep.subway.domain.farepolicy.MoreThan50FarePolicy;

@DisplayName("50km 초과의 요금 정책 테스트")
public class MoreThan50FarePolicyTest {
    @CsvSource({
        "50,0",
        "51,100",
        "59,200",
        "67,300"
    })
    @DisplayName("50km를 초과하면 8km당 100원이 부과된다.")
    @ParameterizedTest
    void calculate(int distance, int cost) {
        FarePolicy farePolicy = new MoreThan50FarePolicy();

        assertThat(farePolicy.calculate(distance)).isEqualTo(cost);
    }
}
