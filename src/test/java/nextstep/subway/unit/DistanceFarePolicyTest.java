package nextstep.subway.unit;

import nextstep.subway.domain.DistanceFarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class DistanceFarePolicyTest {
    @ParameterizedTest(name = "거리: {0}, 가격: {1}")
    @DisplayName("거리별 추가 요금 계산")
    @CsvSource(value = {
            "9,1250",
            "10,1250",
            "11,1350",
            "15,1350",
            "16,1450",
            "50,2050",
            "51,2150",
            "58,2150",
            "59,2250",
    })
    void calculate(int distance, int price) {
        assertThat(new DistanceFarePolicy(distance).calculate()).isEqualTo(price);
    }
}
