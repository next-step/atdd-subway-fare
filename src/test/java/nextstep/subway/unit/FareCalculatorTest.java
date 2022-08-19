package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.domain.path.finder.FareCalculator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareCalculatorTest {

  @ParameterizedTest
  @CsvSource({
      "1, 12, 0, 450",
      "6, 12, 500, 700",
      "10, 18, 0, 720",
      "10, 18, 500, 1120",
      "10, 19, 0, 1250",
      "10, 19, 500, 1750"
  })
  void 요금_계산_10KM_이내(int distance, int age, int maxLineFare, int fare) {
    assertThat(new FareCalculator().calculator(distance, age, maxLineFare)).isEqualTo(fare);
  }

  @ParameterizedTest
  @CsvSource({
      "11, 12, 0, 500",
      "11, 12, 500, 750",
      "16, 18, 0, 880",
      "21, 18, 500, 1360",
      "26, 19, 0, 1650",
      "31, 19, 500, 2250",
      "36, 12, 0, 750",
      "41, 12, 500, 1050",
      "46, 18, 0, 1360",
      "50, 18, 500, 1760"
  })
  void 요금_계산_10KM_초과_50KM_이내(int distance, int age, int maxLineFare, int fare) {
    assertThat(new FareCalculator().calculator(distance, age, maxLineFare)).isEqualTo(fare);
  }

  @ParameterizedTest
  @CsvSource({
      "51, 12, 0, 900",
      "51, 12, 500, 1150",
      "59, 18, 0, 1520",
      "59, 18, 500, 1920",
      "67, 18, 0, 1600",
      "67, 18, 500, 2000",
      "75, 12, 0, 1050"
  })
  void 요금_계산_50KM_초과(int distance, int age, int maxLineFare, int fare) {
    assertThat(new FareCalculator().calculator(distance, age, maxLineFare)).isEqualTo(fare);
  }
}
