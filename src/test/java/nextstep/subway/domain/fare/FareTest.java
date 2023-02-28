package nextstep.subway.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FareTest {

    @Test
    @DisplayName("거리에 따라 적절한 요금을 반환한다.")
    /**
     * 기본운임(10㎞ 이내) : 기본운임 1,250원
     * 이용 거리초과 시 추가운임 부과
     * 10km초과∼50km까지(5km마다 100원)
     * 50km초과 시 (8km마다 100원)
     */
    void calculateFare() {
        Fare fare = new Fare();
        assertThat(fare.calculateFare(10)).isEqualTo(1250);
        assertThat(fare.calculateFare(12)).isEqualTo(1350);
        assertThat(fare.calculateFare(16)).isEqualTo(1450);
        assertThat(fare.calculateFare(75)).isEqualTo(2450);
    }
}