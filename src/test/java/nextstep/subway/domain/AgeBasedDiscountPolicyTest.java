package nextstep.subway.domain;

import static nextstep.subway.domain.fare.AgeBasedDiscountPolicy.ADULT_DISCOUNT_POLICY;
import static nextstep.subway.domain.fare.AgeBasedDiscountPolicy.CHILD_DISCOUNT_POLICY;
import static nextstep.subway.domain.fare.AgeBasedDiscountPolicy.INFANT_DISCOUNT_POLICY;
import static nextstep.subway.domain.fare.AgeBasedDiscountPolicy.JUVENILE_DISCOUNT_POLICY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import nextstep.subway.domain.fare.AgeBasedDiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AgeBasedDiscountPolicyTest {

  @DisplayName("추가요금 적용 대상이 아닌 경우 에러 발생")
  @Test
  void calculate_fail_추가요금_적용_대상_아님() {
    // given
    var fare = 3000;
    var age = 30;

    // when
    var childDiscountResult = catchThrowable(() -> CHILD_DISCOUNT_POLICY.calculate(fare, age));

    // then
    assertThat(childDiscountResult).isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("할인 적용 대상이 아닙니다.");
  }

  @DisplayName("유아에게는 요금을 받지 않는다.")
  @Test
  void calculate_유아_할인금액() {
    // given
    var fare = 1650;
    var age = 2;

    // when
    var discountAmount = INFANT_DISCOUNT_POLICY.calculate(fare, age);

    // then
    assertThat(discountAmount).isEqualTo(fare);
  }

  @DisplayName("어린이 할인금액은 총 요금에서 350원을 제한 값의 50%이다.")
  @Test
  void calculate_어린이_할인금액() {
    // given
    var fare = 1250;
    var age = 8;

    // when
    var discountAmount = CHILD_DISCOUNT_POLICY.calculate(fare, age);

    // then
    assertThat(discountAmount).isEqualTo(450);
  }

  @DisplayName("청소년 할인금액은 총 요금에서 350원을 제한 값의 20%이다.")
  @Test
  void calculate_청소년_할인금액() {
    // given
    var fare = 1650;
    var age = 15;

    // when
    var discountAmount = JUVENILE_DISCOUNT_POLICY.calculate(fare, age);

    // then
    assertThat(discountAmount).isEqualTo(260);
  }

  @DisplayName("성인에게는 할인을 적용하지 않는다.")
  @Test
  void calculate_성인_할인금액() {
    // given
    var fare = 1650;
    var age = 31;

    // when
    var discountAmount = ADULT_DISCOUNT_POLICY.calculate(fare, age);

    // then
    assertThat(discountAmount).isEqualTo(0);
  }

  @DisplayName("나이에 매칭되는 할인 정책을 반환한다.")
  @Test
  void getApplicablePolicy() {
    // given
    int age = 3;

    // when
    var policy = AgeBasedDiscountPolicy.getApplicablePolicy(age);

    // then
    assertThat(policy).isEqualTo(INFANT_DISCOUNT_POLICY);
  }
}