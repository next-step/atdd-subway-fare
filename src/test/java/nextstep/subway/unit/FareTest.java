package nextstep.subway.unit;

import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @Test
    @DisplayName("기본 요금은 1250원이다.")
    void createFare() {
        Fare fare = Fare.of(5);
        assertThat(fare.getFare()).isEqualTo(1250);
    }

    @ParameterizedTest
    @ValueSource(ints = {11, 51, 59})
    @DisplayName("10km초과 ∼ 50km까지는 5km마다 100원의 추가 운임비를 받고, 50km 초과부터는 8km마다 100원의 추가 운임비를 받는다.")
    void getFare_policy(int distance) {
        // when
        Fare fare = Fare.of(distance);

        // then
        if (distance == 11) {
            assertThat(fare.getFare()).isEqualTo(1350);
        }

        if (distance == 51) {
            assertThat(fare.getFare()).isEqualTo(2150);
        }

        if (distance == 59) {
            assertThat(fare.getFare()).isEqualTo(2250);
        }
    }
}
