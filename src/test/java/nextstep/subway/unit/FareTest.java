package nextstep.subway.unit;

import nextstep.subway.domain.pathfinder.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareTest {

    @DisplayName("요금 계산을 위한 거리가 음수 일 수 없습니다")
    @Test
    void fare_invalid_distance() {
        assertThatThrownBy(() -> {new Fare(-1);})
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("요금 계산을 위한 거리가 음수 일 수 없습니다 : -1");
    }

    @DisplayName("거리가 10km 미만이면 요금이 1250원이 계산된다.")
    @Test
    void fee_distance_10km_under() {
        final Fare fare = new Fare(10);

        assertThat(fare.value()).isEqualTo(1250);
    }

    @DisplayName("거리가 10km 초과면 5km마다 100원의 추가요금이 청구된다.")
    @ParameterizedTest
    @CsvSource({ "11, 1350", "16, 1450"})
    void fee_distance_10km_over(int distance, int expectedFare) {
        final Fare fare = new Fare(distance);

        assertThat(fare.value()).isEqualTo(expectedFare);
    }

    @DisplayName("거리가 50km 초과면 8km마다 100원의 추가요금이 청구된다.")
    @ParameterizedTest
    @CsvSource({ "51, 2150", "59, 2250"})
    void fee_distance_50km_over(int distance, int expectedFare) {
        final Fare fare = new Fare(distance);

        assertThat(fare.value()).isEqualTo(expectedFare);
    }
}
