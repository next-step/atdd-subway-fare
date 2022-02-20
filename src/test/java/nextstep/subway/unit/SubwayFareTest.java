package nextstep.subway.unit;

import nextstep.subway.domain.SubwayFare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayFareTest {
    @DisplayName("거리에 따른 지하철 요금을 계산한다.")
    @ParameterizedTest
    @MethodSource(value = "provideDistanceAndFare")
    void calculate(int distance, int fare) {
        // given
        SubwayFare subwayFare = new SubwayFare(distance);

        // when & then
        assertThat(subwayFare.calculate()).isEqualTo(fare);
    }

    private static Stream<Arguments> provideDistanceAndFare() {
        return Stream.of(
                Arguments.of(10, 1250),
                Arguments.of(11, 1350),
                Arguments.of(15, 1350),
                Arguments.of(16, 1450),
                Arguments.of(50, 2050),
                Arguments.of(51, 2150),
                Arguments.of(58, 2150),
                Arguments.of(59, 2250)
        );
    }
}
