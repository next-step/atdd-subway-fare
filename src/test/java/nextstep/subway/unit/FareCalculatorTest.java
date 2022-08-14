package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.domain.path.finder.FareCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareCalculatorTest {

  @Test
  void 요금_계산_10KM_이내() {
    assertThat(FareCalculator.calculator(9)).isEqualTo(1250);
  }

  @ParameterizedTest
  @CsvSource({
      "11, 1350",
      "16, 1450",
      "21, 1550",
      "26, 1650",
      "31, 1750",
      "36, 1850",
      "41, 1950",
      "46, 2050",
      "50, 2050"
  })
  void 요금_계산_10KM_초과_50KM_이내(int distance, int fare) {
    assertThat(FareCalculator.calculator(distance)).isEqualTo(fare);
  }

  @ParameterizedTest
  @CsvSource({
      "51, 2150",
      "59, 2250",
      "67, 2350",
      "75, 2450"
  })
  void 요금_계산_50KM_초과(int distance, int fare) {
    assertThat(FareCalculator.calculator(distance)).isEqualTo(fare);
  }
}
