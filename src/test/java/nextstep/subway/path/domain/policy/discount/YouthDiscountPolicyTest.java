package nextstep.subway.path.domain.policy.discount;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class YouthDiscountPolicyTest {

    @Test
    void discount() {
        // given
        YouthDiscountPolicy policy = new YouthDiscountPolicy();

        // when
        int discounted = policy.discount(1250);

        // then  1250 - ((1250 - 350) * 0.2)
        assertThat(discounted).isEqualTo(1070);
    }
}