package nextstep.subway.path.domain;

import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FareDiscountPolicyTest {

    private final static int DEFAULT_FARE = 1_250;

    @DisplayName("기본요금일 때 청소년 할인 확인")
    @Test
    void defaudefaultFareWithAdditionalFeeltFare() {
        //given
        LoginMember loginMember = new LoginMember(1L, "gpwls@gmail.com","1234", 15);
        FarePolicy fareDistance = new FareDiscountPolicy(loginMember);

        //when
        int fare = fareDistance.calculate(DEFAULT_FARE);

        //then
        assertThat(fare).isEqualTo(1070);
    }

    @DisplayName("기본요금일 때 어린이 할인 확인")
    @Test
    void defaultFareWithTeenagerDiscount() {
        //given
        LoginMember loginMember = new LoginMember(1L, "gpwls@gmail.com","1234", 10);
        FarePolicy fareDistance = new FareDiscountPolicy(loginMember);

        //when
        int fare = fareDistance.calculate(DEFAULT_FARE);

        //then
        assertThat(fare).isEqualTo(800);
    }

    @DisplayName("Anonymous 사용자인 경우 할인 불가.")
    @Test
    public void defaultFareWithAnonymousUer() {
        //given
        LoginMember loginMember = new LoginMember(1L, "","", 10);
        FarePolicy fareDistance = new FareDiscountPolicy(loginMember);

        //when
        int fare = fareDistance.calculate(DEFAULT_FARE);

        //then
        assertThat(fare).isEqualTo(DEFAULT_FARE);
    }

    @DisplayName("로그인 했지만 할인대상이 아닌 경우 할인 불가.")
    @Test
    public void noDiscountBenefit() {
        //given
        LoginMember loginMember = new LoginMember(1L, "gpwls@gmail.com","1234", 33);
        FarePolicy fareDistance = new FareDiscountPolicy(loginMember);

        //when
        int fare = fareDistance.calculate(DEFAULT_FARE);

        //then
        assertThat(fare).isEqualTo(DEFAULT_FARE);
    }
}
