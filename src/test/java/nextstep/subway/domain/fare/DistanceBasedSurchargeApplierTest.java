package nextstep.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceBasedSurchargeApplierTest {

  @DisplayName("10km 이하 거리 추가요금")
  @Test
  void calculate_10km_이하() {
    // given
    var distance = 5;
    var fare = Fare.baseFare();
    var applier = new DistanceBasedSurchargeApplier(distance);

    // when
    applier.calculate(fare);

    // then
    assertThat(fare.getSurcharge()).isEqualTo(0);
    assertThat(fare.getTotalFare()).isEqualTo(1250);
  }

  @DisplayName("10km 초과 50km 미만 거리 추가요금 계산")
  @Test
  void calculate_10km_초과_50km_미만() {
    // given
    var distance = 35;
    var fare = Fare.baseFare();
    var applier = new DistanceBasedSurchargeApplier(distance);

    // when
    applier.calculate(fare);

    // then
    assertThat(fare.getSurcharge()).isEqualTo(500);
    assertThat(fare.getTotalFare()).isEqualTo(1750);
  }

  @DisplayName("50km 초과 거리 추가요금 계산")
  @Test
  void calculate_50km_초과() {
    // given
    var distance = 60;
    var fare = Fare.baseFare();
    var applier = new DistanceBasedSurchargeApplier(distance);

    // when
    applier.calculate(fare);

    // then
    assertThat(fare.getSurcharge()).isEqualTo(1000);
    assertThat(fare.getTotalFare()).isEqualTo(2250);
  }
}