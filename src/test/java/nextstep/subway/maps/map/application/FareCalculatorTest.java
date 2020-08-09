package nextstep.subway.maps.map.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    @DisplayName("기본 요금을 계산한다.")
    @Test
    void calculate() {
        FareCalculator fareCalculator = new FareCalculator();
        int distance = 8;
        int fare = fareCalculator.calculate(distance);
        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("10Km 초과 ~ 50Km까지의 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource({"15, 1350", "16, 1450"})
    void calculateForOverFare(int distance, int result) {
        FareCalculator fareCalculator = new FareCalculator();
        int fare = fareCalculator.calculate(distance);
        assertThat(fare).isEqualTo(result);
    }
}
