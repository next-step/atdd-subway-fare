package nextstep.subway.domain.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {

    @DisplayName("거리는 음수 일 수 없습니다.")
    @Test
    void distance_negative() {
        int distance = -1;

        assertThatThrownBy(() -> { new Distance(distance);})
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("요금 계산을 위한 거리가 음수 일 수 없습니다 : " + distance);
    }
}