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
        int extraFare = 0;
        int fare = fareCalculator.calculate(distance, 0);
        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("10Km 초과 ~ 50Km까지의 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource({"15, 1350, 0", "50, 2050, 0"})
    void calculateFareUnderFiftyKM(int distance, int result, int extraFare) {
        FareCalculator fareCalculator = new FareCalculator();
        int fare = fareCalculator.calculate(distance, extraFare);
        assertThat(fare).isEqualTo(result);
    }

    @DisplayName("10Km 초과 ~ 50Km 추가 요금 노선의 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource({"15, 1550, 200", "50, 2250, 200"})
    void calculateExtraFareUnderFiftyKM(int distance, int result, int extraFare) {
        FareCalculator fareCalculator = new FareCalculator();
        int fare = fareCalculator.calculate(distance, extraFare);
        assertThat(fare).isEqualTo(result);
    }

    @DisplayName("50Km 이상 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource({"51, 2150, 0"})
    void calculateFareOverFiftyKM(int distance, int result, int extraFare) {
        FareCalculator fareCalculator = new FareCalculator();
        int fare = fareCalculator.calculate(distance, extraFare);
        assertThat(fare).isEqualTo(result);
    }
}
