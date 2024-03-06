package nextstep.path.domain;

import nextstep.member.domain.AgeType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class DiscountConditionTest {

    @Test
    @DisplayName("청소년 할인 요금")
    void discountTeenager() {
        DiscountCondition discountCondition = DiscountConditionFactory.createDiscountCondition(AgeType.getAgeType(14));
        Assertions.assertThat(discountCondition).isInstanceOf(TeenagerDiscountCondition.class);
        Assertions.assertThat(discountCondition.discount(1500)).isEqualTo(920);
    }

    @Test
    @DisplayName("어린이 할인 요금")
    void discountChild() {
        DiscountCondition discountCondition = DiscountConditionFactory.createDiscountCondition(AgeType.getAgeType(8));
        Assertions.assertThat(discountCondition).isInstanceOf(ChildDiscountCondition.class);
        Assertions.assertThat(discountCondition.discount(1500)).isEqualTo(575);
    }


    @DisplayName("성인 할인 요금")
    @Test
    void discountAdult() {
        DiscountCondition discountCondition = DiscountConditionFactory.createDiscountCondition(AgeType.getAgeType(20));
        Assertions.assertThat(discountCondition).isInstanceOf(NoneDiscountCondition.class);
        Assertions.assertThat(discountCondition.discount(1500)).isEqualTo(1500);
    }

}