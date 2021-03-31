package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.discount.ChildrenDiscountPolicy;
import nextstep.subway.path.domain.policy.discount.YouthDiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountTest {

    @DisplayName("어린이 할인")
    @Test
    void discount() {
        // given
        ChildrenDiscountPolicy childrenDiscountPolicy = new ChildrenDiscountPolicy();

        // when
        int fare = Discount.excute(1250, childrenDiscountPolicy);

        // then
        assertThat(fare).isEqualTo(800);
    }

    @DisplayName("청소년 할인")
    @Test
    void discount2() {
        // given
        YouthDiscountPolicy youthDiscountPolicy = new YouthDiscountPolicy();

        // when
        int fare = Discount.excute(1250, youthDiscountPolicy);

        // then
        assertThat(fare).isEqualTo(1070);
    }
}
