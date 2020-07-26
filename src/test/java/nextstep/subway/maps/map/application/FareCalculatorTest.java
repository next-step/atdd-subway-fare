package nextstep.subway.maps.map.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class FareCalculatorTest {

    @DisplayName("기본 요금을 계산한다.")
    @Test
    void calculate() {
        // given
        FareCalculator fareCalculator = new FareCalculator();

        // when
        int distance = 0;
        int fare = fareCalculator.calculate(distance);

        // then
        Assertions.assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("10km초과∼50km까지(5km마다 100원)")
    @ParameterizedTest
    @CsvSource({"13, 1350", "50, 2050"})
    public void calculateForOverFare(int distance, int result) {
        FareCalculator fareCalculator = new FareCalculator();

        // when
        int fare = fareCalculator.calculate(distance);

        // then
        Assertions.assertThat(fare).isEqualTo(result);
    }

    @DisplayName("50km초과 시 (8km마다 100원)")
    @ParameterizedTest
    @CsvSource({"57, 2150", "66, 2250"})
    public void calculateForExtraOverFare(int distance, int result) {
        FareCalculator fareCalculator = new FareCalculator();

        // when
        int fare = fareCalculator.calculate(distance);

        // then
        Assertions.assertThat(fare).isEqualTo(result);
    }
}