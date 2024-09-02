package nextstep.subway.fare.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import nextstep.subway.fare.domain.FarePolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FarePolicyTest {

    @Test
    void testBaseFare() {
        assertEquals(1250, FarePolicy.BASE_FARE.calculateFare(5));
        assertEquals(1250, FarePolicy.BASE_FARE.calculateFare(100));
    }

    @ParameterizedTest
    @CsvSource({
        "9, 0",
        "10, 0",
        "11, 100",
        "15, 100",
        "49, 800",
        "50, 800",
        "51, 800",
        "58, 800",
        "100, 800"
    })
    void testDistanceBasedFare(int distance, int expectedFare) {
        assertEquals(expectedFare, FarePolicy.DISTANCE_BASED.calculateFare(distance));
    }

    @ParameterizedTest
    @CsvSource({
        "49, 0",
        "50, 0",
        "51, 100",
        "58, 100",
        "59, 200",
        "100, 700"
    })
    void testLongDistanceFare(int distance, int expectedFare) {
        assertEquals(expectedFare, FarePolicy.LONG_DISTANCE.calculateFare(distance));
    }

    @ParameterizedTest
    @CsvSource({
        "5, 1250",
        "10, 1250",
        "15, 1350",
        "25, 1550",
        "50, 2050",
        "51, 2150",
        "100, 2750"
    })
    void testCalculateTotalFare(int distance, int expectedTotalFare) {
        assertEquals(expectedTotalFare, FarePolicy.calculateTotalFare(distance));
    }

    @Test
    void testCalculateTotalFareWithAdditionalFares() {
        assertEquals(2150, FarePolicy.calculateTotalFare(25, Arrays.asList(200L, 600L)));
        assertEquals(1550, FarePolicy.calculateTotalFare(25, Collections.emptyList()));
        assertEquals(1550, FarePolicy.calculateTotalFare(25, null));
    }

    @Test
    void testGetMaxAdditionalFare() {
        assertEquals(600, FarePolicy.calculateTotalFare(25, Arrays.asList(200L, 600L, null)) - FarePolicy.calculateTotalFare(25));
        assertEquals(0, FarePolicy.calculateTotalFare(25, Collections.emptyList()) - FarePolicy.calculateTotalFare(25));
    }
}
