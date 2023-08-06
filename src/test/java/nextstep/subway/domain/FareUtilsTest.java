package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class FareUtilsTest {

    @DisplayName("5km마다 100원인 경우 추가 금액 계산")
    @ParameterizedTest
    @CsvSource(value = {"0,0", "1,100", "2,100", "3,100", "4,100", "5,100", "6,200", "10,200", "11,300", "15,300", "16,400", "20,400", "21,500"})
    void calculateOverFare_interval_5_overFarePerInterval_100(int overDistance, int expectedOverFare) {
        int actual = FareUtils.calculateOverFare(overDistance, 5, 100);
        assertEquals(expectedOverFare, actual);
    }

    @DisplayName("8km마다 100원인 경우 추가 금액 계산")
    @ParameterizedTest
    @CsvSource(value = {"0,0", "1,100", "2,100", "3,100", "4,100", "5,100", "6,100", "7,100", "8,100", "9,200", "16,200", "17,300"})
    void calculateOverFare_interval_8(int overDistance, int expectedOverFare) {
        int actual = FareUtils.calculateOverFare(overDistance, 8, 100);
        assertEquals(expectedOverFare, actual);
    }

}