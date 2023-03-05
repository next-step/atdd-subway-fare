package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareTypeTest {
    @DisplayName("거리별 기준에 맞게 요금이 부과된다.")
    @ParameterizedTest
    @CsvSource(value = {
            "9, 1250",
            "10, 1250",
            "11, 1350",
            "14, 1350",
            "15, 1350",
            "16, 1450",
            "19, 1450",
            "20, 1450",
            "21, 1550",
            "49, 2050",
            "50, 2050",
            "51, 2150",
            "58, 2150",
            "59, 2250",
    })
    void calculateFare(int distance, int expectedFare) {
        int actualFare = FareType.calculateFare(distance);
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @DisplayName("거리가 0 이거나 음수이면 요금을 계산할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void doNotCalculateFare(int distance) {
        assertThatThrownBy(() -> FareType.calculateFare(distance)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리가 0 이거나 음수일 경우 요금 계산할 수 없습니다.");
    }
}