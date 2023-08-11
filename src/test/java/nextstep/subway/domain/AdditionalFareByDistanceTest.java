package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class AdditionalFareByDistanceTest {

    @DisplayName("거리에 따른 추가 요금을 확인")
    @ParameterizedTest(name = "[{index}] 거리가 {0}km인 경우, 요금은 {1}원이다.")
    @CsvSource(value = {"8,1250", "10,1250", "11,1350", "16,1450", "50,2050", "51,2150", "58,2150", "59,2250", "80,2450", "100,2750"})
    void fare(int distance, int expected) {
        AdditionalFareByDistance fareCalculator = new AdditionalFareByDistance(distance);
        assertEquals(expected, fareCalculator.fare(1250));
    }
}