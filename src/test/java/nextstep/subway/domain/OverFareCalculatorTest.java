package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OverFareCalculatorTest {

    @DisplayName("5km마다 100원인 경우 추가 금액 계산")
    @ParameterizedTest(name = "[{index}] 초과 거리가 {0}km인 경우, 추가 금액은 {1}원이다.")
    @CsvSource(value = {"0,0", "1,100", "2,100", "3,100", "4,100", "5,100", "6,200", "10,200", "11,300", "15,300", "16,400", "20,400", "21,500"})
    void calculateOverFare_interval_5_overFarePerInterval_100(int overDistance, int expectedOverFare) {
        int actual = new OverFareCalculator(5, 100).calculateOverFare(overDistance);
        assertEquals(expectedOverFare, actual);
    }

    @DisplayName("8km마다 100원인 경우 추가 금액 계산")
    @ParameterizedTest(name = "[{index}] 초과 거리가 {0}km인 경우, 추가 금액은 {1}원이다.")
    @CsvSource(value = {"0,0", "1,100", "2,100", "3,100", "4,100", "5,100", "6,100", "7,100", "8,100", "9,200", "16,200", "17,300"})
    void calculateOverFare_interval_8(int overDistance, int expectedOverFare) {
        int actual = new OverFareCalculator(8, 100).calculateOverFare(overDistance);
        assertEquals(expectedOverFare, actual);
    }

}