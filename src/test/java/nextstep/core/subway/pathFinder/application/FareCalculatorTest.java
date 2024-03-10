package nextstep.core.subway.pathFinder.application;

import nextstep.core.subway.line.domain.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class FareCalculatorTest {

    Line 이호선;
    Line 신분당선;
    Line 삼호선;
    Line 사호선;

    FareCalculator fareCalculator;

    @BeforeEach
    void setUp() {
        fareCalculator = new FareCalculator();
    }

    @BeforeEach
    void 사전_노선_설정() {
        이호선 = new Line("이호선", "green", 0);
        신분당선 = new Line("신분당선", "red", 400);
        삼호선 = new Line("삼호선", "orange", 800);
        사호선 = new Line("사호선", "blue", 600);
    }

    @ParameterizedTest
    @DisplayName("거리에 기반해서 요금이 계산된다.")
    @CsvSource(
            value = {"9:1250", "10:1250", "11:1350", "25:1550", "46:2050", "50:2050", "57:2150", "58:2150", "59:2250", "74:2350"},
            delimiter = ':')
    void 요금_계산(int 거리, int 요금) {
        // when
        int fare = fareCalculator.calculateFare(거리);

        // then
        assertThat(fare).isEqualTo(요금);
    }
}
