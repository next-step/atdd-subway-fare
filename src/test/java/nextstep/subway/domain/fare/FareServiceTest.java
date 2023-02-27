package nextstep.subway.domain.fare;

import nextstep.subway.domain.Line;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FareServiceTest {

    @Test
    @DisplayName("거리에 따라 적절한 요금을 반환한다.")
    /**
     * 기본운임(10㎞ 이내) : 기본운임 1,250원
     * 이용 거리초과 시 추가운임 부과
     * 10km초과∼50km까지(5km마다 100원)
     * 50km초과 시 (8km마다 100원)
     */
    void calculateFare() {
        FareService fareService = new FareService();
        assertThat(fareService.calculateFare(10)).isEqualTo(1250);
        assertThat(fareService.calculateFare(12)).isEqualTo(1350);
        assertThat(fareService.calculateFare(16)).isEqualTo(1450);
        assertThat(fareService.calculateFare(75)).isEqualTo(2450);
    }

    @Test
    @DisplayName("추가 요금이 있는 노선의 경우 추가요금을 포함한 요금을 반환한다.")
    void calculateExtraFare() {
        FareService fareService = new FareService();
        final int defaultFare = fareService.calculateFare(10);
        List<Line> lines = Arrays.asList(new Line("이호선", "green", 100),
                new Line("삼호선", "yellow", 0));
        assertThat(fareService.calculateExtraFare(defaultFare, lines)).isEqualTo(1350);
    }
}