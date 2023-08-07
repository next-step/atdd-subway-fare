package nextstep.subway.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FareCalculatorTest {

    @ParameterizedTest
    @CsvSource(value = {"8,1250", "10,1250", "11,1350", "16,1450", "50,2050", "51,2150", "58,2150", "59,2250", "80,2450", "100,2750"})
    void fare(int distance, int expected) {
        FareCalculator fareCalculator = new FareCalculator(distance, 0);
        assertEquals(expected, fareCalculator.fare());
    }

    @ParameterizedTest
    @CsvSource(value = {"8,1350", "10,1350", "11,1450", "16,1550", "50,2150", "51,2250", "58,2250", "59,2350", "80,2550", "100,2850"})
    void fare_with_fareByLine(int distance, int expected) {
        int additionalFare = 100;
        FareCalculator fareCalculator = new FareCalculator(distance, additionalFare);
        assertEquals(expected, fareCalculator.fare());
    }
}