package nextstep.subway.unit;

import static nextstep.subway.domain.FarePolicy.createFarePolicy;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.domain.FarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTest {
    /**
     * given 경로 길이가 주어지면
     * when (10KM 부터 5km초과 될때마다 100원 추가, 50km부터 8km초과 마다 100원 추가)
     *     when - 경로 거리가 n KM 일 경우 (n KM)
     * then 경로 거리에 맞는 요금이 조회된다.
     */
    @ParameterizedTest
    @CsvSource({
            "9, 1250",
            "10, 1250",
            "11, 1350",
            "14, 1350",
            "15, 1350",
            "16, 1450",
            "20, 1450",
            "21, 1550",
            "25, 1550",
            "26, 1650",
            "30, 1650",
            "31, 1750",
            "35, 1750",
            "36, 1850",
            "40, 1850",
            "41, 1950",
            "45, 1950",
            "46, 2050",
            "50, 2050",
            "57, 2150",
            "58, 2150",
            "59, 2250",
            "59, 2250",
            "65, 2250",
            "66, 2250",
            "67, 2350",
    })
    @DisplayName("요금 계산")
    void calculateFare(int 경로_거리, int 요금 ) {
        // when
        FarePolicy farePolicy = createFarePolicy(경로_거리);
        int fare = farePolicy.getFare(경로_거리);

        // then
        assertThat(fare).isEqualTo(요금);
    }
}
