package nextstep.subway.unit;

import nextstep.subway.domain.AgeDiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.domain.AgeDiscountPolicy.INVALID_AGE_MESSAGE;
import static nextstep.subway.domain.FarePolicy.DEFAULT_FARE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("나이별 요금 할인 정책 테스트")
class AgeDiscountPolicyTest {

    @DisplayName("5세 이하 영유아는 무료")
    @Test
    void baby() {
        AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.of(5);
        assertThat(ageDiscountPolicy.discount(DEFAULT_FARE)).isEqualTo(0);
    }

    @DisplayName("6세 이상 13세 미만 어린이는 운임에서 350원을 공제한 금액의 50%할인")
    @Test
    void children() {
        AgeDiscountPolicy ageOf6 = AgeDiscountPolicy.of(6);
        assertThat(ageOf6.discount(DEFAULT_FARE)).isEqualTo(450);

        AgeDiscountPolicy ageOf12 = AgeDiscountPolicy.of(12);
        assertThat(ageOf12.discount(DEFAULT_FARE)).isEqualTo(450);
    }

    @DisplayName("13세 이상 19세 미만 청소년은 운임에서 350원을 공제한 금액의 20%할인")
    @Test
    void teenager() {
        AgeDiscountPolicy ageOf13 = AgeDiscountPolicy.of(13);
        assertThat(ageOf13.discount(DEFAULT_FARE)).isEqualTo(720);

        AgeDiscountPolicy ageOf18 = AgeDiscountPolicy.of(18);
        assertThat(ageOf18.discount(DEFAULT_FARE)).isEqualTo(720);
    }

    @DisplayName("19세 이상 어른은 기본 운임비")
    @Test
    void adult() {
        AgeDiscountPolicy ageOf19 = AgeDiscountPolicy.of(19);
        assertThat(ageOf19.discount(DEFAULT_FARE)).isEqualTo(1250);
    }

    @DisplayName("0 또는 음수는 예외 발생")
    @Test
    void exception() {
        assertThatThrownBy(() -> AgeDiscountPolicy.of(0), INVALID_AGE_MESSAGE)
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> AgeDiscountPolicy.of(-1), INVALID_AGE_MESSAGE)
                .isInstanceOf(IllegalArgumentException.class);
    }
}