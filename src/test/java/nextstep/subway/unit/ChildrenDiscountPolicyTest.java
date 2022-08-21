package nextstep.subway.unit;

import nextstep.subway.domain.discount.ChildrenDiscountPolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class ChildrenDiscountPolicyTest {

    @Test
    void childrenDiscount() {
        ChildrenDiscountPolicy childrenDiscountPolicy = new ChildrenDiscountPolicy();
        int fare = 1000;

        int result = childrenDiscountPolicy.discount(fare);

        assertThat(result).isEqualTo((int)((fare - 350) * 0.5));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 5, 6, 10, 13, 19})
    void childrenSupport(int age) {
        ChildrenDiscountPolicy childrenDiscountPolicy = new ChildrenDiscountPolicy();

        boolean result = childrenDiscountPolicy.support(age);

        assertThat(result).isEqualTo(age >= 6 && age < 13);
    }

}
