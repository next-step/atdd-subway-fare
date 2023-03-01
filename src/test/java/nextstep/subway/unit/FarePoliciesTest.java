package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.policy.FareDiscountPolicies;
import nextstep.subway.domain.policy.calculate.CalculateConditions;
import nextstep.subway.domain.policy.FareCalculatePolicies;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static nextstep.subway.domain.policy.calculate.CalculateConditions.CalculateConditionBuilder;
import static org.assertj.core.api.Assertions.assertThat;

class FarePoliciesTest {

    private FareCalculatePolicies fareCalculatePolicies;
    private FareDiscountPolicies fareDiscountPolicies;


    @BeforeEach
    void setup() {
        //given 요금 정책을 확정한다.
        fareCalculatePolicies = new FareCalculatePolicies();
        fareDiscountPolicies = new FareDiscountPolicies();
    }

    @ParameterizedTest
    @DisplayName("거리 비례 요금 계산")
    @CsvSource(value = {"8,1250", "10,1250", "12,1350", "16,1450", "50,2050", "60,2250", "65, 2250"})
    void calculateFare(int distance, int fare) {
        //when 거리를 넣어 요금을 계산한다.
        CalculateConditions conditions = new CalculateConditionBuilder(distance).build();
        //then 거리에 따라 계산된 요금이 반환된다.
        assertThat(fareCalculatePolicies.calculate(conditions)).isEqualTo(fare);
    }

    @ParameterizedTest
    @CsvSource(value = {"6, 850", "12, 850", "13, 1360", "18, 1360", "19, 2050", "20, 2050"})
    @DisplayName("연령별 요금 할인 계산")
    void ageDiscount(int age, int fare) {
        //given 연령을 계산 조건으로 설정한다.
        CalculateConditions conditions = new CalculateConditionBuilder(50)
                .age(age)
                .build();
        //then 요금을 계산한다.
        int calculated = fareCalculatePolicies.calculate(conditions);
        calculated = fareDiscountPolicies.discount(conditions, calculated);
        //then 연령별로 할인된 요금이 반환된다.
        assertThat(calculated).isEqualTo(fare);
    }

    @Test
    @DisplayName("노선 추가 요금 계산")
    void lineSurcharge() {
        //given 노선별 추가 요금을 계산 조건으로 설정한다.
        Line a = new Line("a", "color-a", 1000);
        Line b = new Line("b", "color-b", 500);
        CalculateConditions conditions = new CalculateConditionBuilder(50)
                .lines(Arrays.asList(a,b))
                .build();
        //when 요금을 계산한다
        int calculated = fareCalculatePolicies.calculate(conditions);
        calculated = fareDiscountPolicies.discount(conditions, calculated);
        //then 노선별 추가요금이 더해진 요금이 반환된다.
        assertThat(calculated).isEqualTo(3050);
    }

}
