package nextstep.subway.path.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DistanceFarePolicyTest {
    private DistanceFarePolicy distanceFarePolicy;

    @Test
    void calculateFareByPolicyTest_WithDistance58() {
        distanceFarePolicy = new DistanceFarePolicy(58);

        assertThat(distanceFarePolicy.calculateFareByPolicy(1250)).isEqualTo(2150);
    }

    @Test
    void calculateFareByPolicyTest_WithDistanceUnder10() {
        distanceFarePolicy = new DistanceFarePolicy(5);

        assertThat(distanceFarePolicy.calculateFareByPolicy(1250)).isEqualTo(1250);
    }

    @Test
    void calculateFareByPolicyTest_WithDistance15() {
        distanceFarePolicy = new DistanceFarePolicy(15);

        assertThat(distanceFarePolicy.calculateFareByPolicy(1250)).isEqualTo(1350);
    }
}
