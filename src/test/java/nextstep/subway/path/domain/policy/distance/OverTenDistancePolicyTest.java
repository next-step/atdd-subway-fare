package nextstep.subway.path.domain.policy.distance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class OverTenDistancePolicyTest {

    @DisplayName("거리 10킬로 넘을 때 요금 계산")
    @ParameterizedTest
    @CsvSource(value = {"11:1350", "16:1450", "26:1650"},  delimiter = ':')
    void calculate(int distance, int expectedFare) {
        // given
        int basicFare = 1250;
        OverTenDistancePolicy policy = new OverTenDistancePolicy(distance);

        // when
        int fare = policy.calculateFare(basicFare);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}
