package nextstep.subway.domain.fare;

import nextstep.subway.domain.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FareServiceTest {

    private FareService fareService;

    @BeforeEach
    void setUp() {
        fareService = new FareService();
    }

    @Test
    void 십km_이내_거리는_기본_요금() {
        assertThat(fareService.calculateFare(10)).isEqualTo(1250);
    }

    @Test
    void 십km_초과_시_추가운임() {
        assertThat(fareService.calculateFare(12)).isEqualTo(1350);
    }

    @Test
    void 오십km_초과_시_추가운임() {
        assertThat(fareService.calculateFare(51)).isEqualTo(2150);
    }

    @Test
    @DisplayName("추가 요금이 있는 노선의 경우 추가요금을 포함한 요금을 반환한다.")
    void calculateExtraFare() {
        final int defaultFare = fareService.calculateFare(10);
        List<Line> lines = Arrays.asList(new Line("이호선", "green", 100),
                new Line("삼호선", "yellow", 0));
        assertThat(fareService.calculateExtraFare(defaultFare, lines)).isEqualTo(1350);
    }
}