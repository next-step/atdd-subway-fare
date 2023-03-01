package nextstep.subway.unit;

import nextstep.subway.domain.Fare.FarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
public class FarePoliceTest {
    @DisplayName("운행 거리가 10이하인 경우 기본 요금인 1250원이다.")
    @MethodSource("defaultDistance")
    @ParameterizedTest
    void distanceIsDefault(int distance) {
        final int fare = FarePolicy.getFare(distance);

        assertThat(fare).isEqualTo(1250);
    }

    private static IntStream defaultDistance() {
        return IntStream.range(1, 10);
    }

    @DisplayName("운행 거리가 10km 초과 50km 이하인 경우 기본요금에 5km 마다 100원 할증이 붙는다.")
    @MethodSource("betweenTenAndFifty")
    @ParameterizedTest(name = "거리: {0}, 요금: {1}")
    void distanceBetween10And50(int distance, int expectedFare) {
        final int fare = FarePolicy.getFare(distance);

        assertThat(fare).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> betweenTenAndFifty() {
        return Stream.of(
                Arguments.of(11, 1350),
                Arguments.of(15, 1350),
                Arguments.of(16, 1450),
                Arguments.of(20, 1450),
                Arguments.of(21, 1550),
                Arguments.of(25, 1550),
                Arguments.of(26, 1650),
                Arguments.of(30, 1650),
                Arguments.of(31, 1750),
                Arguments.of(35, 1750),
                Arguments.of(36, 1850),
                Arguments.of(40, 1850),
                Arguments.of(41, 1950),
                Arguments.of(45, 1950),
                Arguments.of(46, 2050),
                Arguments.of(50, 2050)
        );
    }

    @DisplayName("운행거리가 50km 초과인 경우 50km 이상의 거리에 대해서는 8km 마다 100원 할증이 붙는다.")
    @MethodSource("overFifty")
    @ParameterizedTest(name = "거리: {0}, 요금: {1}")
    void distanceOverFifty(int distance, int expectedFare) {
        final int fare = FarePolicy.getFare(distance);

        assertThat(fare).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> overFifty() {
        return Stream.of(
                Arguments.of(51, 2150),
                Arguments.of(58, 2150),
                Arguments.of(59, 2250),
                Arguments.of(66, 2250),
                Arguments.of(67, 2350),
                Arguments.of(74, 2350),
                Arguments.of(75, 2450),
                Arguments.of(344, 5750)
        );
    }
}
