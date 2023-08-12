package nextstep.subway.domain;

import nextstep.subway.domain.fare.FareCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FareCalculatorTest {
    private static final int NO_ADDITIONAL_FARE = 0;

    @DisplayName("추가 요금/할인이 없는 경우, 거리에 따른 기본 요금을 확인")
    @ParameterizedTest(name = "[{index}] 거리가 {0}km인 경우, 요금은 {1}원이다.")
    @CsvSource(value = {"8,1250", "10,1250", "11,1350", "16,1450", "50,2050", "51,2150", "58,2150", "59,2250", "80,2450", "100,2750"})
    void fare(int distance, int expected) {
        FareCalculator fareCalculator = new FareCalculator(distance, NO_ADDITIONAL_FARE, Optional.empty());
        assertEquals(expected, fareCalculator.fare());
    }

    @DisplayName("노선에 따른 추가 요금이 없고, 나이에 따른 할인이 있는 경우, 거리에 따른 요금을 확인")
    @ParameterizedTest(name = "[{index}] 나이가 {0}세이고, 거리가 {1}km인 경우, 요금은 {2}원이다.")
    @CsvSource(value = {"0,100,0", "5,10,0", "6,10,800", "6,20,900", "6,100,1550", "12,100,1550", "13,10,1070", "13,20,1230", "13,100,2270", "18,100,2270", "19,100,2750"})
    void fareWithAge(int age, int distance, int expected) {
        FareCalculator fareCalculator = new FareCalculator(distance, NO_ADDITIONAL_FARE, Optional.of(age));
        assertEquals(expected, fareCalculator.fare());
    }

    @DisplayName("나이에 따른 할인이 없고, 노선에 따른 추가 요금이 100원인 경우, 거리에 따른 요금을 확인")
    @ParameterizedTest(name = "[{index}] 노선에 따른 추가요금이 100원이고 거리가 {0}km인 경우, 요금은 {1}원이다.")
    @CsvSource(value = {"8,1350", "10,1350", "11,1450", "16,1550", "50,2150", "51,2250", "58,2250", "59,2350", "80,2550", "100,2850"})
    void fare_with_fareByLine(int distance, int expected) {
        int additionalFare = 100;
        FareCalculator fareCalculator = new FareCalculator(distance, additionalFare, Optional.empty());
        assertEquals(expected, fareCalculator.fare());
    }
}