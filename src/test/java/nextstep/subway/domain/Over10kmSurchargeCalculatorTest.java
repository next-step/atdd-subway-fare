package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Over10kmSurchargeCalculatorTest {

  Over10kmSurchargeCalculator calculator = new Over10kmSurchargeCalculator();

  @DisplayName("10km 이하 이동거리에 대한 추가요금은 0원이다.")
  @Test
  void calculate_10km_이하_이동거리() {
    // given
    var distance = 10;

    // when
    var surcharge = calculator.calculate(distance);

    // then
    assertThat(surcharge).isEqualTo(0);
  }

  @DisplayName("5km 마다 100원의 추가요금을 부과한다.")
  @Test
  void calculate_5km_마다_100원의_추가요금() {
    // given
    var distance = 20;

    // when
    var surcharge = calculator.calculate(distance);

    // then
    assertThat(surcharge).isEqualTo(200);
  }

  @DisplayName("50km를 초과하는 이동거리에 대해서는 50km 까지만 추가요금을 부과한다.")
  @Test
  void calculate_50km_초과_이동거리에_대한_추가요금_부과() {
    // given
    var distance = 51;

    // when
    var surcharge = calculator.calculate(distance);

    // then
    assertThat(surcharge).isEqualTo(800);
  }

  @DisplayName("단위 거리당 추가요금을 계산한 뒤 나머지 거리는 올림으로 계산한다.")
  @Test
  void calculate_나누어_떨어지지_않는_거리는_올림해서_계산() {
    // given
    var distance = 13;

    // when
    var surcharge = calculator.calculate(distance);

    // then
    assertThat(surcharge).isEqualTo(100);
  }
}