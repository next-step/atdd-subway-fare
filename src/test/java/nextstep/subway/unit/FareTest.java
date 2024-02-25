package nextstep.subway.unit;

import nextstep.subway.domain.pathfinder.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareTest {

    @DisplayName("요금 계산을 위한 거리가 음수 일 수 없습니다")
    @Test
    void fare_invalid_distance() {
        assertThatThrownBy(() -> {new Fare(-10);})
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("요금 계산을 위한 거리가 음수 일 수 없습니다 : -10");
    }

    @DisplayName("거리가 10km 미만이면 요금이 1250원이 계산된다.")
    @Test
    void fee_distance_10km_under() {
        final Fare fare = new Fare(10);

        assertThat(fare.value()).isEqualTo(1250);
    }

    @DisplayName("거리가 10km 초과면 5km마다 100원의 추가요금이 청구된다.")
    @Test
    void fee_distance_10km_over() {
        final Fare fare = new Fare(11);
        final Fare fare1 = new Fare(16);

        assertThat(fare.value()).isEqualTo(1350);
        assertThat(fare1.value()).isEqualTo(1450);
    }

    @DisplayName("거리가 50km 초과면 8km마다 100원의 추가요금이 청구된다.")
    @Test
    void fee_distance_50km_over() {
        final Fare fare = new Fare(51);
        final Fare fare1 = new Fare(59);

        assertThat(fare.value()).isEqualTo(2150);
        assertThat(fare1.value()).isEqualTo(2250);
    }

}
