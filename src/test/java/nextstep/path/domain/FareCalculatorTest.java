package nextstep.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    private final FareCalculator calculator = new FareCalculator();

    @DisplayName("요금 계산")
    @ParameterizedTest
    @CsvSource({
            "9,1250",  "10,1250", // 10km 미만일 때 기본 운임
            "11,1350", "14,1350", // 10km 초과시 5km 마다 100원 추가
            "15,1450", "19,1450",
            "20,1550", "24,1550",
            "25,1650", "29,1650",
            "30,1750", "34,1750",
            "35,1850", "39,1850",
            "40,1950", "44,1950",
            "45,2050", "49,2050",
            "50,2150", "57,2150", // 50km 부터 8km 마다 100원 추가
            "58,2250", "65,2250"
    })
    void calculateFare(int distance, int expectedFare) {
        assertThat(calculator.calculateFare(distance)).isEqualTo(expectedFare);
    }
}
