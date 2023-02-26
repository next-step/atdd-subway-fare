package nextstep.subway.unit;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nextstep.subway.domain.Fare;

class FareTest {

    @DisplayName("지하철 구간 길이가 10km 이내라면, 이용 요금은 기본 요금이다")
    @MethodSource("basicDistanceFareSource")
    @ParameterizedTest
    void basicDistanceFare(int distance, int lineExtraFare, int age, int fare) {
        assertThat(Fare.calculate(distance, lineExtraFare, age)).isEqualTo(fare);
    }

    @DisplayName("지하철 구간 길이가 10km 초과 50km 이하라면, 기본 요금에 5km 마다 100원씩 요금이 추가된다.")
    @MethodSource("middleDistanceFareSource")
    @ParameterizedTest
    void middleDistanceFare(int distance, int lineExtraFare, int age, int fare) {
        assertThat(Fare.calculate(distance, lineExtraFare, age)).isEqualTo(fare);
    }

    @DisplayName("지하철 구간 길이가 50km 초과라면, 기본 요금에 8km 마다 100원씩 요금이 추가된다.")
    @MethodSource("longDistanceFareSource")
    @ParameterizedTest
    void longDistanceFare(int distance, int lineExtraFare, int age, int fare) {
        assertThat(Fare.calculate(distance, lineExtraFare, age)).isEqualTo(fare);
    }

    private static Stream<Arguments> basicDistanceFareSource() {
        return Stream.of(
            Arguments.of(1, 0, 6, 800),
            Arguments.of(1, 0, 13, 1_070),
            Arguments.of(1, 0, 20, 1_250),
            Arguments.of(10, 0, 20, 1_250),
            Arguments.of(10, 500, 20, 1_750)
        );
    }

    private static Stream<Arguments> middleDistanceFareSource() {
        return Stream.of(
            Arguments.of(11, 0, 6, 850),
            Arguments.of(11, 0, 13, 1_150),
            Arguments.of(11, 0, 20, 1_350),
            Arguments.of(50, 0, 20, 2_050),
            Arguments.of(50, 500, 20, 2_550)
        );
    }

    private static Stream<Arguments> longDistanceFareSource() {
        return Stream.of(
            Arguments.of(51, 0, 6, 1_100),
            Arguments.of(51, 0, 13, 1_550),
            Arguments.of(51, 0, 20, 1_850),
            Arguments.of(67, 0, 20, 2_050),
            Arguments.of(67, 500, 20, 2_550)
        );
    }
}
