package nextstep.subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class AdditionalFareByDistanceTest {

    @ParameterizedTest
    @CsvSource(value = {"8,1250", "10,1250", "11,1350", "16,1450", "50,2050", "51,2150", "58,2150", "59,2250", "80,2450", "100,2750"})
    void fare(int distance, int expected) {
        AdditionalFareByDistance fareCalculator = new AdditionalFareByDistance(distance);
        assertEquals(expected, fareCalculator.fare(1250));
    }
}