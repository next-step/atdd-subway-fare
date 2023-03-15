package nextstep.subway.unit;

import nextstep.member.domain.LoginMember;
import nextstep.member.domain.RoleType;
import nextstep.subway.domain.AgeDiscountPolicy;
import nextstep.subway.domain.DiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.domain.DistanceFarePolicy.DEFAULT_FARE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AgeDiscountPolicyTest {

    @Test
    @DisplayName("0 이하 나이 입력")
    void lessThan1Age() {
        // given
        final DiscountPolicy discountPolicy = new AgeDiscountPolicy(new LoginMember(-1L, 0, List.of(RoleType.ROLE_MEMBER.name())));

        // when
        // then
        assertThatThrownBy(() -> discountPolicy.calculator(DEFAULT_FARE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("나이는 0 이하일 수 없습니다.");
    }

    @Test
    @DisplayName("비로그인 요금 할인")
    void anonymousDiscount() {
        // given
        final DiscountPolicy discountPolicy = new AgeDiscountPolicy(new LoginMember(-1L, 0, List.of(RoleType.ROLE_ANONYMOUS.name())));

        // when
        final int result = discountPolicy.calculator(DEFAULT_FARE);

        // then
        assertThat(result).isEqualTo(1250);
    }

    @Test
    @DisplayName("요금 할인 없음")
    void noneDiscount() {
        // given
        final DiscountPolicy discountPolicy = new AgeDiscountPolicy(new LoginMember(-1L, 20, List.of(RoleType.ROLE_MEMBER.name())));

        // when
        final int result = discountPolicy.calculator(DEFAULT_FARE);

        // then
        assertThat(result).isEqualTo(1250);
    }

    @Test
    @DisplayName("청소년 요금 할인")
    void teenagerDiscount() {
        // given
        final DiscountPolicy discountPolicy = new AgeDiscountPolicy(new LoginMember(-1L, 13, List.of(RoleType.ROLE_MEMBER.name())));

        // when
        final int result = discountPolicy.calculator(DEFAULT_FARE);

        // then
        assertThat(result).isEqualTo(1070);
    }

    @Test
    @DisplayName("어린이 요금 할인")
    void childDiscount() {
        // given
        final DiscountPolicy discountPolicy = new AgeDiscountPolicy(new LoginMember(-1L, 6, List.of(RoleType.ROLE_MEMBER.name())));

        // when
        final int result = discountPolicy.calculator(DEFAULT_FARE);

        // then
        assertThat(result).isEqualTo(800);
    }
}
