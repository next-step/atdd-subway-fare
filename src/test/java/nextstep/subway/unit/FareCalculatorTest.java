package nextstep.subway.unit;

import nextstep.subway.domain.FareCalculator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareCalculatorTest {

    @DisplayName("9km 거리 요금 계산")
    @Test
    void calculate_9km_fare() {
        Assertions.assertThat(FareCalculator.calculateFare(9))
                .isEqualTo(1250);
    }

    @DisplayName("12km 거리 요금 계산")
    @Test
    void calculate_12km_fare() {
        Assertions.assertThat(FareCalculator.calculateFare(12))
                .isEqualTo(1350);
    }

    @DisplayName("16km 거리 요금 계산")
    @Test
    void calculate_16km_fare() {
        Assertions.assertThat(FareCalculator.calculateFare(16))
                .isEqualTo(1450);
    }

    @DisplayName("55km 거리 요금 계산")
    @Test
    void calculate_55km_fare() {
        Assertions.assertThat(FareCalculator.calculateFare(55))
                .isEqualTo(2150);
    }
}
