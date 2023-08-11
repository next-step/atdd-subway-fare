package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeBasedDiscountPolicyTest {

    @DisplayName("연령별 요금 할인정책을 반환한다")
    @ParameterizedTest
    @CsvSource({
            "0,FREE",
            "5,FREE",
            "6,CHILD_DISCOUNT",
            "12,CHILD_DISCOUNT",
            "13,TEENAGER_DISCOUNT",
            "18,TEENAGER_DISCOUNT",
            "19,NO_DISCOUNT"
    })
    void testReturnAgeBasedFareDiscountPolicy(int age, AgeBasedDiscountPolicy expect) {
        assertThat(AgeBasedDiscountPolicy.getPolicyByAge(age)).isEqualTo(expect);
    }
}
