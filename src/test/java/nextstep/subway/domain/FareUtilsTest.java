package nextstep.subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class FareUtilsTest {

    @ParameterizedTest
    @CsvSource(value = {"0,0", "1,100", "2,100", "3,100", "4,100", "5,100", "6,200", "10,200", "11,300", "15,300", "16,400", "20,400", "21,500"})
    void calculateOverFare_interval_5(int overDistance, int expectedOverFare) {
        assertEquals(expectedOverFare, FareUtils.calculateOverFare(5, overDistance));
    }

    @ParameterizedTest
    @CsvSource(value = {"0,0", "1,100", "2,100", "3,100", "4,100", "5,100", "6,100", "7,100", "8,100", "9,200", "16,200", "17,300"})
    void calculateOverFare_interval_8(int overDistance, int expectedOverFare) {
        assertEquals(expectedOverFare, FareUtils.calculateOverFare(8, overDistance));
    }

}