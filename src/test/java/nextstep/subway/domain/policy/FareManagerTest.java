package nextstep.subway.domain.policy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareManagerTest {

    @DisplayName("10km 까지는 기본 요금을 반환한다.")
    @ParameterizedTest(name = "#{index} - 거리={0}km")
    @ValueSource(ints = {1, 10})
    void fare(int distance) {
        assertThat(FareManager.fare(distance)).isEqualTo(1_250);
    }

    @DisplayName("10km 초과 ~ 50km 이하의 경우 기본운임과 5km 마다 100원 추가된다.")
    @ParameterizedTest(name = "#{index} - 거리={0}km, 추가요금={1}")
    @CsvSource(value = {"11:1350", "16:1450", "21:1550", "26:1650", "31:1750", "36:1850", "41:1950", "46:2050", "50:2050"}, delimiter = ':')
    void fare_extra(int distance, int expected) {
        assertThat(FareManager.fare(distance)).isEqualTo(expected);
    }

    @DisplayName("50km 초과시 기본운임과 50km 이하의 초과운임과 8km 마다 100원이 추가된다.")
    @ParameterizedTest(name = "#{index} - 거리={0}km, 추가요금={1}")
    @CsvSource(value = {"51:2150", "59:2250", "67:2350"}, delimiter = ':')
    void fare_extra_over_fifty(int distance, int expected) {
        assertThat(FareManager.fare(distance)).isEqualTo(expected);
    }
}