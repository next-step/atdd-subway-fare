package nextstep.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private static User 로그인_유저_19살 = new Member("email@email.com", "1234", 19);
    private static User 로그인_유저_13살 = new Member("email@email.com", "1234", 13);
    private static User 로그인_유저_6살 = new Member("email@email.com", "1234", 6);
    private static User 비로그인_유저 = new Guest();
    private static final int FARE = 1250;

    @Test
    @DisplayName("로그인 회원 중 19세 이상의 경우 할인금액을 적용하지 않는다.")
    void applyUserFarePolicy() {
        assertThat(로그인_유저_19살.applyFarePolicy(FARE)).isEqualTo(1250);
    }

    @Test
    @DisplayName("로그인 회원 중 13세 이상 19세 미만의 경우 350원 공제 후 20% 할인금액을 적용한다.")
    void applyUserFarePolicy2() {
        assertThat(로그인_유저_13살.applyFarePolicy(FARE)).isEqualTo(720);
    }

    @Test
    @DisplayName("로그인 회원 중 6세 이상 13세 미만의 경우 350원 공제 후 50% 할인금액을 적용한다.")
    void applyUserFarePolicy3() {
        assertThat(로그인_유저_6살.applyFarePolicy(FARE)).isEqualTo(450);
    }

    @Test
    @DisplayName("비로그인 회원의 경우 할인금액을 적용하지 않는다.")
    void applyGuestFarePolicy() {
        assertThat(비로그인_유저.applyFarePolicy(FARE)).isEqualTo(1250);
    }
}
