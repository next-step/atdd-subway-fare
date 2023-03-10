package nextstep.subway.unit.fare;

import nextstep.subway.domain.fare.DistanceFareType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceFareTypeTest {
    @DisplayName("기본 운임 (10km 이내)으로 1250 원의 요금이 부과된다.")
    @ParameterizedTest
    @CsvSource(value = {
            "1, 1250",
            "5, 1250",
            "9, 1250",
            "10, 1250"
    })
    void basicFare(int distance, int expectedFare) {
        int actualFare = DistanceFareType.calculateFare(distance);
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @DisplayName("추가 운임 (10km 초과, 50km 까지)으로 기본운임(1250원)에서 5km 마다 100 원의 요금이 추가로 부과된다.")
    @ParameterizedTest
    @CsvSource(value = {
            "11, 1350",
            "14, 1350",
            "15, 1350",
            "16, 1450",
            "19, 1450",
            "20, 1450",
            "21, 1550",
            "49, 2050",
            "50, 2050",
    })
    void over10kmFare(int distance, int expectedFare) {
        int actualFare = DistanceFareType.calculateFare(distance);
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @DisplayName("추가 운임 (50km 초과)으로 50km 까지 계산된 추가운임(2050원)에서 8km 마다 100원의 요금이 추가로 부과된다.")
    @ParameterizedTest
    @CsvSource(value = {
            "51, 2150",
            "58, 2150",
            "59, 2250",
            "66, 2250",
            "67, 2350",
    })
    void over50kmFare(int distance, int expectedFare) {
        int actualFare = DistanceFareType.calculateFare(distance);
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @DisplayName("거리가 0 이거나 음수이면 요금을 계산할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void doNotCalculateFare(int distance) {
        assertThatThrownBy(() -> DistanceFareType.calculateFare(distance)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리가 0 이거나 음수일 경우 요금 계산할 수 없습니다.");
    }
}