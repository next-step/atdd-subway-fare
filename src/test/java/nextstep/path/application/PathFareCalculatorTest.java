package nextstep.path.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class PathFareCalculatorTest {

    @ParameterizedTest
    @CsvSource(value =
            {
                    "10,1250",
                    "11,1350",
                    "16,1450",
                    "21,1550",
                    "46,2050",
                    "51,2150",
                    "59,2250",
                    "67,2350"
            }, delimiterString = ",")
    @DisplayName("거리 대비 요금을 계산할 수 있다.")
    void calculateFareTest(final int distance, final long expected) {
        final PathFareCalculator pathFareCalculator = new PathFareCalculator();
        final long fare = pathFareCalculator.calculate(distance);

        assertThat(fare).isEqualTo(expected);
    }
}
