package nextstep.subway.unit.farepolicy;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.domain.farepolicy.BasicFarePolicy;
import nextstep.subway.domain.farepolicy.DistanceFarePolicy;
import nextstep.subway.domain.farepolicy.FarePolicy;

@DisplayName("거리별 요금 부과 정책")
public class DistanceFarePolicyTest {
    @CsvSource({
        "9,1250",
        "10,1250",
        "11,1350",
        "50,2050",
        "51,2150",
    })
    @DisplayName("거리별 요금을 계산한다.")
    @ParameterizedTest
    void calculate(int distance, int totalCost) {
        FarePolicy farePolicy = new DistanceFarePolicy(Arrays.asList());

        assertThat(farePolicy.calculate(distance))
            .isEqualTo(totalCost);
    }
}
