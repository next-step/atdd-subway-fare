package nextstep.subway.path.domain.policy.discount;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChildrenDiscountPolicyTest {

    @Test
    void discount() {
        // given
        ChildrenDiscountPolicy policy = new ChildrenDiscountPolicy();

        // when
        int discounted = policy.calculateFare(1250);

        // then  1250 - ((1250 - 350) * 0.5)
        assertThat(discounted).isEqualTo(800);
    }
}
