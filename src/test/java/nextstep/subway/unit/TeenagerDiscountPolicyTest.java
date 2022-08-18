package nextstep.subway.unit;

import nextstep.subway.domain.discount.TeenagerDiscountPolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class TeenagerDiscountPolicyTest {

    @Test
    void teenagerDiscount() {
        TeenagerDiscountPolicy teenagerDiscountPolicy = new TeenagerDiscountPolicy();
        int fare = 1000;

        int result = teenagerDiscountPolicy.discount(fare);

        assertThat(result).isEqualTo((int)((fare - 350) * 0.8));
    }

    @ParameterizedTest
    @ValueSource(ints = {12, 13, 15, 18, 19})
    void teenagerSupport(int age) {
        TeenagerDiscountPolicy teenagerDiscountPolicy = new TeenagerDiscountPolicy();

        boolean result = teenagerDiscountPolicy.support(age);

        assertThat(result).isEqualTo(age >= 13 && age < 19);
    }
}
