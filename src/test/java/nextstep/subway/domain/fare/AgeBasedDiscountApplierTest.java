package nextstep.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AgeBasedDiscountApplierTest {

  @DisplayName("유아는 전체 요금을 할인받는다.")
  @Test
  void calculate_유아() {
    // given
    var age = 3;
    var fare = Fare.baseFare();
    var applier = new AgeBasedDiscountApplier(age);

    // when
    applier.calculate(fare);

    // then
    assertThat(fare.getDiscountAmount()).isEqualTo(1250);
    assertThat(fare.getTotalFare()).isEqualTo(0);
  }

  @DisplayName("어린이는 총 요금에서 350원을 제한 금액의 50퍼센트 할인된 요금을 낸다.")
  @Test
  void calculate_어린이() {
    // given
    var age = 8;
    var fare = Fare.baseFare();
    var applier = new AgeBasedDiscountApplier(age);

    // when
    applier.calculate(fare);

    // then
    assertThat(fare.getDiscountAmount()).isEqualTo(450);
    assertThat(fare.getTotalFare()).isEqualTo(800);
  }

  @DisplayName("청소년은 총 요금에서 350원을 제한 금액의 20퍼센트 할인된 요금을 낸다.")
  @Test
  void calculate_청소년() {
    // given
    var age = 15;
    var fare = Fare.baseFare();
    var applier = new AgeBasedDiscountApplier(age);

    // when
    applier.calculate(fare);

    // then
    assertThat(fare.getDiscountAmount()).isEqualTo(180);
    assertThat(fare.getTotalFare()).isEqualTo(1070);
  }

  @DisplayName("성인은 나이 할인을 적용하지 않는다.")
  @Test
  void calculate_성인() {
    // given
    var age = 31;
    var fare = Fare.baseFare();
    var applier = new AgeBasedDiscountApplier(age);

    // when
    applier.calculate(fare);

    // then
    assertThat(fare.getDiscountAmount()).isEqualTo(0);
    assertThat(fare.getTotalFare()).isEqualTo(1250);
  }
}