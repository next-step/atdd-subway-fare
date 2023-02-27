package nextstep.subway.unit;

import nextstep.subway.domain.policy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FarePoliciesTest {

    private FarePolicies farePolicies;


    @BeforeEach
    void setup() {
        //given 요금 정책을 확정한다.
        farePolicies = new FarePolicies();
    }

    @ParameterizedTest
    @CsvSource(value = {"8,1250", "10,1250", "12,1350", "16,1450", "50,2050", "60,2250", "65, 2250"})
    void calculateFare(int distance, int fare) {
        //then 거리를 넣으면 요금이 계산된다.
        assertThat(farePolicies.calculate(distance, 20)).isEqualTo(fare);
    }

}
