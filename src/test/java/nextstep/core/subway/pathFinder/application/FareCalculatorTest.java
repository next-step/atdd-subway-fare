package nextstep.core.subway.pathFinder.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class FareCalculatorTest {

    FareCalculator fareCalculator;

    @BeforeEach
    void setUp() {
        fareCalculator = new FareCalculator();
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
