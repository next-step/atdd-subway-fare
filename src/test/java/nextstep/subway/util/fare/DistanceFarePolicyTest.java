package nextstep.subway.util.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFarePolicyTest {
    @DisplayName("10km 이하의 거리는 1250원의 요금이 발생한다.")
    @CsvSource(value = {"1:1250", "10:1250"}, delimiter = ':')
    @ParameterizedTest
    void calculate(int distance, int fare) {
        assertThat(DistanceFarePolicy.calculate(distance)).isEqualTo(fare);
    }

    @DisplayName("10km 초과의 거리는 5km당 100원씩 추가요금이 발생한다.")
    @CsvSource(value = {"11:1350", "15:1350", "50:2050"}, delimiter = ':')
    @ParameterizedTest
    void calculateOver10km(int distance, int fare) {
        assertThat(DistanceFarePolicy.calculate(distance)).isEqualTo(fare);
    }

    @DisplayName("50km 초과의 거리는 8km당 100원씩 추가요금이 발생한다.")
    @CsvSource(value = {"51:2150", "58:2150", "59:2250"}, delimiter = ':')
    @ParameterizedTest
    void calculateOver50km(int distance, int fare) {
        assertThat(DistanceFarePolicy.calculate(distance)).isEqualTo(fare);
    }
}
