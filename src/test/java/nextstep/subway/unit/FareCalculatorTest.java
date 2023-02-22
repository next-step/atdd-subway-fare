package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nextstep.subway.util.FareCalculator;

class FareCalculatorTest {

    @DisplayName("지하철 구간 길이에 대한 요금을 계산한다.")
    @MethodSource("calculateFareSource")
    @ParameterizedTest
    void calculate(int distance, int fare) {
        assertThat(FareCalculator.calculate(distance)).isEqualTo(fare);
    }

    private static Stream<Arguments> calculateFareSource() {
        return Stream.of(
            Arguments.of(10, 1_250),
            Arguments.of(12, 1_350),
            Arguments.of(16, 1_450),
            Arguments.of(50, 2_050),
            Arguments.of(58, 1_850),
            Arguments.of(64, 1_950)
        );
    }
}
