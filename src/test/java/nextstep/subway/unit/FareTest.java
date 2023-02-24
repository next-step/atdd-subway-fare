package nextstep.subway.unit;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.domain.Fare;

class FareTest {

    @DisplayName("지하철 구간 길이가 10km 이내라면, 이용 요금은 기본 요금이다")
    @ValueSource(ints = {1, 10})
    @ParameterizedTest
    void basicDistanceFare(int distance) {
        assertThat(Fare.calculate(distance)).isEqualTo(1_250);
    }

    @DisplayName("지하철 구간 길이가 10km 초과 50km 이하라면, 기본 요금에 5km 마다 100원씩 요금이 추가된다.")
    @MethodSource("middleDistanceFareSource")
    @ParameterizedTest
    void middleDistanceFare(int distance, int fare) {
        assertThat(Fare.calculate(distance)).isEqualTo(fare);
    }

    @DisplayName("지하철 구간 길이가 50km 초과라면, 기본 요금에 8km 마다 100원씩 요금이 추가된다.")
    @MethodSource("longDistanceFareSource")
    @ParameterizedTest
    void longDistanceFare(int distance, int fare) {
        assertThat(Fare.calculate(distance)).isEqualTo(fare);
    }

    private static Stream<Arguments> middleDistanceFareSource() {
        return Stream.of(
            Arguments.of(10, 1_250),
            Arguments.of(12, 1_350),
            Arguments.of(16, 1_450),
            Arguments.of(50, 2_050)
        );
    }

    private static Stream<Arguments> longDistanceFareSource() {
        return Stream.of(
            Arguments.of(51, 1_850),
            Arguments.of(59, 1_950),
            Arguments.of(67, 2_050)
        );
    }
}
