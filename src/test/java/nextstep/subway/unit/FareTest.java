package nextstep.subway.unit;

import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @Test
    @DisplayName("기본 요금은 1250원이다.")
    void createFare() {
        Fare fare = Fare.of(5);
        assertThat(fare.getFare()).isEqualTo(1250);
    }

    @Test
    @DisplayName("거리가 11km 이면 요금은 1350원 이다.")
    void getFare_distance_11() {
        // given
        int 거리 = 11;

        // when
        Fare fare = Fare.of(거리);

        // then
        assertThat(fare.getFare()).isEqualTo(1350);
    }

    @Test
    @DisplayName("거리가 51km 이면 요금은 2150원 이다.")
    void getFare_distance_51() {
        // given
        int 거리 = 51;

        // when
        Fare fare = Fare.of(거리);

        // then
        assertThat(fare.getFare()).isEqualTo(2150);
    }

    @Test
    @DisplayName("거리가 59km 이면 요금은 2250원 이다.")
    void getFare_distance_59() {
        // given
        int 거리 = 59;

        // when
        Fare fare = Fare.of(거리);

        // then
        assertThat(fare.getFare()).isEqualTo(2250);
    }
}
