package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("요금 정책 관련 기능")
class FarePolicyTest {

    @DisplayName("거리 기준으로 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(
            value = {"1:1250", "10:1250", "11:1350", "50:2050", "51:2150", "58:2150", "59:2250", "66:2250", "67:2350"},
            delimiter = ':'
    )
    void calculatorOverFareByDistance(int distance, int fare) {
        FarePolicy farePolicy = new DistanceFarePolicy(new BasicDistanceFareFormula());

        assertThat(farePolicy.apply(distance)).isEqualTo(fare);
    }
}
