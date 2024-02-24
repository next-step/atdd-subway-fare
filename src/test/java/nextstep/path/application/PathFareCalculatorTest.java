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
                    "15,1350",
                    "20,1450",
                    "45,1950",
                    "50,2050",
                    "58,2150",
                    "66,2250"
            }, delimiterString = ",")
    @DisplayName("거리가 대비 요금을 계산할 수 있다.")
    void calculateFareTest(final int distance, final long expected) {
        final PathFareCalculator pathFareCalculator = new PathFareCalculator();
        final long fare = pathFareCalculator.calculate(distance);

        assertThat(fare).isEqualTo(expected);
    }
}
