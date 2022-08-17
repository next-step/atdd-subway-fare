package nextstep.subway.unit;

import nextstep.subway.domain.FareCalculator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareCalculatorTest {

    @DisplayName("9km 거리 요금 계산")
    @Test
    void calculate_9km_fare() {
        Assertions.assertThat(FareCalculator.calculateFareWithDistance(9))
                .isEqualTo(1_250);
    }

    @DisplayName("10km 거리 요금 계산")
    @Test
    void calculate_10km_fare() {
        Assertions.assertThat(FareCalculator.calculateFareWithDistance(10))
                .isEqualTo(1_250);
    }

    @DisplayName("12km 거리 요금 계산")
    @Test
    void calculate_12km_fare() {
        Assertions.assertThat(FareCalculator.calculateFareWithDistance(12))
                .isEqualTo(1_350);
    }

    @DisplayName("16km 거리 요금 계산")
    @Test
    void calculate_16km_fare() {
        Assertions.assertThat(FareCalculator.calculateFareWithDistance(16))
                .isEqualTo(1_450);
    }

    @DisplayName("50km 거리 요금 계산")
    @Test
    void calculate_50km_fare() {
        Assertions.assertThat(FareCalculator.calculateFareWithDistance(50))
                .isEqualTo(2_050);
    }

    @DisplayName("55km 거리 요금 계산")
    @Test
    void calculate_55km_fare() {
        Assertions.assertThat(FareCalculator.calculateFareWithDistance(55))
                .isEqualTo(2_150);
    }
}
