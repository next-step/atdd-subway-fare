package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FareTest {

  @DisplayName("경로의 총 길이별로 요금을 계산한다")
  @ParameterizedTest
  @MethodSource("testParameters")
  void calculate(int kilometer, long price) {
    //given
    Fare fare = new Fare(kilometer);
    //when
    long cost = fare.getCost();
    //then
    assertThat(cost).isEqualTo(price);
  }

  private static Stream<Arguments> testParameters() {
    return Stream.of(
        Arguments.of(10, 1250),
        Arguments.of(15, 1350),
        Arguments.of(57, 2050),
        Arguments.of(58, 2150)
    );
  }


}