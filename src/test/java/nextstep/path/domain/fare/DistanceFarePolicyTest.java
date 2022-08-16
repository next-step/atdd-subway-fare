package nextstep.path.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFarePolicyTest {

    @DisplayName("거리 기반 요금 계산")
    @ParameterizedTest
    @CsvSource({
            "10,0", // 10km 미만일 때 기본 운임
            "11,100", // 10km 초과시 5km 마다 100원 추가
            "15,200",
            "45,800",
            "50,900", // 50km 부터 8km 마다 100원 추가
            "58,1000",
    })
    void apply(int distance, int expectedFare) {
        FarePolicy policy = new DistanceFarePolicy(distance, new NullFarePolicy());

        int result = policy.apply(0);

        assertThat(result).isEqualTo(expectedFare);
    }
}
