package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AgeFarePolicyTest {


    @DisplayName("나잇값이 음수일 때 에러가 발생한다.")
    @Test
    void ageNegativeNumberException() {
        //given
        FarePolicy policy = new AgeFarePolicy(-1);

        //when, then
        assertThatThrownBy(() -> policy.proceed(1250))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 나이로 요금을 계산할 수 없습니다.");
    }

    @DisplayName("나이가 어린이 일 때 (350 원 공제 후 50% 할인)")
    @Test
    void childrenFare() {
        //given
        FarePolicy policy = new AgeFarePolicy(6);

        //when
        int fare = policy.proceed(1250);

        //then
        assertThat(fare).isEqualTo((int) ((1_250 - 350) * 0.5));
    }

    @DisplayName("나이가 청소년 일 때 (350 원 공제 후 20% 할인)")
    @Test
    void teenagerFare() {
        //given
        FarePolicy policy = new AgeFarePolicy(13);

        //when
        int fare = policy.proceed(1250);

        //then
        assertThat(fare).isEqualTo((int) ((1_250 - 350) * 0.8));
    }

    @DisplayName("나이가 성인이면 공제되지 않는다")
    @Test
    void test() {
        //given
        FarePolicy policy = new AgeFarePolicy(25);

        //when
        int fare = policy.proceed(1250);

        //then
        assertThat(fare).isEqualTo(1250);
    }
}