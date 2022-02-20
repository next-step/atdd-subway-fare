package nextstep.subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;

class FareLineStrategyTest {

    private static final Fare 영원 = Fare.of(BigDecimal.ZERO);
    private static final Fare 천원 = Fare.of(BigDecimal.valueOf(1_000));
    private static final Fare 이천원 = Fare.of(BigDecimal.valueOf(2_000));
    private static final Fare 삼천원 = Fare.of(BigDecimal.valueOf(3_000));

    private Line 신분당선;
    private Line 일호선;
    private Line 이호선;
    private Line 삼호선;
    private Line 사호선;
    private FareLineStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new FareLineStrategy();

        신분당선 = Line.of("신분당선", "red", 삼천원);
        일호선 = Line.of("일호선", "blue", 이천원);
        이호선 = Line.of("이호선", "green", 천원);
        삼호선 = Line.of("삼호선", "orange", 영원);
        사호선 = Line.of("사호선", "blue", 영원);
    }

    @DisplayName("노선중 가장 높은 금액을 반환한다")
    @Test
    void calculate() {
        // given
        Path path = Mockito.mock(Path.class);
        Mockito.when(path.getLines())
                .thenReturn(Arrays.asList(신분당선, 일호선, 이호선));

        // when
        Fare fare = strategy.calculate(path);

        // then
        Assertions.assertThat(fare).isEqualTo(삼천원);
    }

    @DisplayName("노선이 모두 요금이 0원이면 0원을 반환한다")
    @Test
    void calculate_default() {
        // given
        Path path = Mockito.mock(Path.class);
        Mockito.when(path.getLines())
                .thenReturn(Arrays.asList(삼호선, 사호선));

        // when
        Fare fare = strategy.calculate(path);

        // then
        Assertions.assertThat(fare).isEqualTo(영원);
    }

}