package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.policy.CalculateConditions;
import nextstep.subway.domain.policy.FarePolicies;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static nextstep.subway.domain.policy.CalculateConditions.CalculateConditionBuilder;
import static org.assertj.core.api.Assertions.assertThat;

class FarePoliciesTest {

    private FarePolicies farePolicies;


    @BeforeEach
    void setup() {
        //given 요금 정책을 확정한다.
        farePolicies = new FarePolicies();
    }

    @ParameterizedTest
    @DisplayName("거리 비례 요금 계산")
    @CsvSource(value = {"8,1250", "10,1250", "12,1350", "16,1450", "50,2050", "60,2250", "65, 2250"})
    void calculateFare(int distance, int fare) {
        //then 거리를 넣으면 요금이 계산된다.

        CalculateConditions conditions = new CalculateConditionBuilder(distance).build();

        assertThat(farePolicies.calculate(conditions)).isEqualTo(fare);
    }

    @ParameterizedTest
    @CsvSource(value = {"6, 850", "12, 850", "13, 1360", "18, 1360", "19, 2050", "20, 2050"})
    @DisplayName("연령별 요금 할인 계산")
    void ageDiscount(int age, int fare) {
        //when 연령을 계산 조건으로 설정한다.
        CalculateConditions conditions = new CalculateConditionBuilder(50)
                .age(age)
                .build();
        //then 요금이 할인된다.
        assertThat(farePolicies.calculate(conditions)).isEqualTo(fare);
    }

    @Test
    @DisplayName("노선 추가 요금 계산")
    void lineSurcharge() {
        //when 노선별 추가 요금을 계산 조건으로 설정한다.
        Line a = new Line("a", "color-a", 1000);
        Line b = new Line("b", "color-b", 500);
        CalculateConditions conditions = new CalculateConditionBuilder(50)
                .lines(Arrays.asList(a,b))
                .build();
        //then 요금이 할인된다.
        assertThat(farePolicies.calculate(conditions)).isEqualTo(3050);
    }

}
