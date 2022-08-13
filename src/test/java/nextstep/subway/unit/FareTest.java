package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTest {
    /**
     * given 길이가 주어지면
     * when (10KM 부터 5km초과 될때마다 100원 추가, 50km부터 8km 마다 100원 추가)
     *     when - 경로 거리가 9 KM 일 경우 (9KM)
     *     when - 경로 거리가 10 KM 일 경우 (10KM)
     *     when - 경로 거리가 10 KM 초과일 경우 (10.1 KM)
     *     when - 경로 거리가 14 KM 일 경우 (14 KM)
     *     when - 경로 거리가 15 KM 일 경우 (15 KM)
     *     when - 경로 거리가 16 KM 일 경우 (16 KM)
     *     when - 경로 거리가 50 KM 일 경우 (50 KM)
     *     when - 경로 거리가 50 KM 초과일 경우 (50.1 KM)
     *     when - 경로 거리가 57 KM 일 경우 (57 KM)
     *     when - 경로 거리가 58 KM 일 경우 (58 KM)
     *     when - 경로 거리가 65 KM 일 경우 (65 KM)
     * then 경로 거리에 맞는 요금이 조회된다.
     */
    @ParameterizedTest
    @CsvSource({
            "9, 1250",
            "10, 1250",
            "10.1, 1350",
            "14,1350",
            "15, 1450",
            "16,1450",
            "50, 2150",
            "50.1, 2150",
            "57, 2150",
            "58, 2250",
            "65, 2250"
    })
    @DisplayName("요금 계산")
    void calculateFare(int 경로_거리, int 요금 ) {
        // given
        // when
        // then
    }

}
