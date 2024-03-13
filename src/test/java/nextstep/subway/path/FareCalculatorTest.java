package nextstep.subway.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class FareCalculatorTest {

    @DisplayName("10km 이하 기본 요금 계산")
    @Test
    void basicFareCalculate() {
        //when
        int fare = FareCalculator.calculate(10);

        //then
        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("50km 이하 10km 초과 요금 계산")
    @Test
    void under50KmOver10KmFareCalculate() {
        //when
        int fare = FareCalculator.calculate(30);

        //then
        assertThat(fare).isEqualTo(1750);
    }

    @DisplayName("50km 초과 요금 계산")
    @Test
    void over50KmFareCalculate() {
        //when
        int fare = FareCalculator.calculate(105);

        //then
        assertThat(fare).isEqualTo(2750);
    }
}