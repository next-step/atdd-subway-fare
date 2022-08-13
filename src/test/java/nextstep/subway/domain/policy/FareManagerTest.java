package nextstep.subway.domain.policy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareManagerTest {

    @DisplayName("10km 까지는 기본 요금을 반환한다.")
    @ParameterizedTest(name = "#{index} - 거리={0}km")
    @ValueSource(ints = {1, 10})
    void fare(int distance) {
        assertThat(FareManager.fare(distance)).isEqualTo(1_250);
    }

}