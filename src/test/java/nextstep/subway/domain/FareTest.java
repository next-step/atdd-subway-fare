package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FareTest {
    @DisplayName("길이가 10km 이하라면, 기본 운임으로 계산된다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    void baseFare(int distance) {
        assertThat(Fare.calculate(distance)).isEqualTo(1250);
    }

    @DisplayName("길이가 10km 초과 및 50km 이하라면, 기본 운임에 5km 당 100원의 추가 운임이 추가된다.")
    @ParameterizedTest
    @CsvSource({"11,1350", "50,2050"})
    void middleFare(int distance, int fare) {
        assertThat(Fare.calculate(distance)).isEqualTo(fare);
    }

    @DisplayName("길이가 50km 초과라면, 기본 운임에 8km 당 100원의 추가 운임이 추가된다.")
    @ParameterizedTest
    @CsvSource({"51,1850", "60,1950"})
    void longFare(int distance, int fare) {
        assertThat(Fare.calculate(distance)).isEqualTo(fare);
    }
}
