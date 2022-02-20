package nextstep.subway.path.domain;

import nextstep.subway.path.dto.FarePolicyRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    @DisplayName("거리비례 요금정책 적용시 요금 계산")
    @ParameterizedTest
    @CsvSource({"5,1250", "31,1750", "51,2150"})
    void distancePolicyCalculate(int distance, int expectedFare) {
        // given
        List<FarePolicy> farePolicies = Arrays.asList(DistancePolicy.choicePolicyByDistance(distance));
        FareCalculator fareCalculator = FareCalculator.from(farePolicies);

        // when
        int fare = fareCalculator.calculate(createFarePolicyRequest(distance));

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