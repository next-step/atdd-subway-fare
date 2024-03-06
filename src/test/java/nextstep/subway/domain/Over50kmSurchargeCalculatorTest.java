package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Over50kmSurchargeCalculatorTest {

  Over50kmSurchargeCalculator calculator = new Over50kmSurchargeCalculator();

  @DisplayName("50km 이하 이동거리에 대한 추가요금은 0원이다.")
  @Test
  void calculate_50km_이하_이동거리() {
    // given
    var distance = 50;

    // when
    var surcharge = calculator.calculate(distance);

    // then
    assertThat(surcharge).isEqualTo(0);
  }

  @DisplayName("8km 마다 100원의 추가요금을 부과한다.")
  @Test
  void calculate_8km_마다_100원의_추가요금() {
    // given
    var distance = 58;

    // when
    var surcharge = calculator.calculate(distance);

    // then
    assertThat(surcharge).isEqualTo(100);
  }

  @DisplayName("단위 거리당 추가요금을 계산한 뒤 나머지 거리는 올림으로 계산한다.")
  @Test
  void calculate_나누어_떨어지지_않는_거리는_올림해서_계산() {
    // given
    var distance = 59;

    // when
    var surcharge = calculator.calculate(distance);

    // then
    assertThat(surcharge).isEqualTo(200);
  }
}