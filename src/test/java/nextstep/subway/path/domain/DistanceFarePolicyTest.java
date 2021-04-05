package nextstep.subway.path.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DistanceFarePolicyTest {
    private DistanceFarePolicy distanceFarePolicy;
    private static final int BASIC_FEE = 1250;

    @ParameterizedTest
    @CsvSource(value = {"58:2150", "5:1250", "15:1350"}, delimiter = ':')
    void calculateFareByPolicyTest_WithDistance58(int distance, int fare) {
        distanceFarePolicy = new DistanceFarePolicy(distance);

        assertThat(distanceFarePolicy.calculateFareByPolicy(BASIC_FEE)).isEqualTo(fare);
    }
}
