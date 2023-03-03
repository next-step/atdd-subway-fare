package nextstep.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DiscountPolicyTest {

    @DisplayName("800 = (1250 - 350) * 0.5 + 350")
    @Test
    void children_discount() {
        assertThat(DiscountPolicy.CHILDREN.calculateDiscountFare(1250)).isEqualTo(800);
    }

    @DisplayName("1070 = (1250 - 350) * 0.8 + 350")
    @Test
    void teenager_discount() {
        assertThat(DiscountPolicy.TEENAGER.calculateDiscountFare(1250)).isEqualTo(1070);
    }

    @DisplayName("1250 = 1250")
    @Test
    void adult_discount() {
        assertThat(DiscountPolicy.ADULT.calculateDiscountFare(1250)).isEqualTo(1250);
    }
}
