package nextstep.subway.path.domain;

import nextstep.subway.path.dto.FarePolicyRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    @DisplayName("거리비례 요금정책 적용시 요금 계산")
    @ParameterizedTest
    @CsvSource({"5,1250", "31,1750", "51,2150"})
    void distancePolicyCalculate(int distance, int expectedFare) {
        // when
        int fare = FareCalculator.calculate(createFarePolicyRequest(distance));

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    private FarePolicyRequest createFarePolicyRequest(int distance) {
        return FarePolicyRequest
                .builder()
                .distance(distance)
                .build();
    }
}