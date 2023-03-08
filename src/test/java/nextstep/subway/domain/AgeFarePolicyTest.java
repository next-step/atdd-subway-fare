package nextstep.subway.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeFarePolicyTest {

    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void 청소년_요금_정책(int age) {
        AgeFarePolicy policy = AgeFarePolicy.of(age);
        int fare = policy.calculateFare(1350);

        assertThat(policy).isEqualTo(AgeFarePolicy.쳥소년);
        assertThat(fare).isEqualTo((int) ((1350 - 350) * 0.8));
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void 어린이_요금_정책(int age) {
        AgeFarePolicy policy = AgeFarePolicy.of(age);
        int fare = policy.calculateFare(1350);

        assertThat(policy).isEqualTo(AgeFarePolicy.어린이);
        assertThat(fare).isEqualTo((int) ((1350 - 350) * 0.5));
    }
}