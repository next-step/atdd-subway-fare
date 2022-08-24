package nextstep.subway.util.fare;

import nextstep.subway.domain.Line;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineSurchargePolicyTest {
    private Line 일호선 = new Line("1호선", "green", 1000);
    private Line 이호선 = new Line("2호선", "orange", 1200);
    private Line 삼호선 = new Line("3호선", "blue", 300);
    private Line 사호선 = new Line("4호선", "red", 0);
    private Line 구호선 = new Line("9호선", "yellow", 0);
    private Line 신분당선 = new Line("신분당선", "brown", 0);

    private List<Line> 지하철_노선_목록;

    @DisplayName("노선 중 가장 비싼 추가요금을 반환한다.")
    @Test
    void calculate() {
        지하철_노선_목록 = List.of(일호선, 이호선, 삼호선);

        assertThat(LineSurchargePolicy.calculate(지하철_노선_목록)).isEqualTo(1200);
    }

    @DisplayName("지나는 모든 노선의 추가요금이 없는 경우 0원을 반환한다.")
    @Test
    void calculateNotSurcharge() {
        지하철_노선_목록 = List.of(사호선, 구호선, 신분당선);

        assertThat(LineSurchargePolicy.calculate(지하철_노선_목록)).isZero();
    }
}
