package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {
    @DisplayName("경로에 따른 요금을 계산한다.")
    @ParameterizedTest
    @MethodSource("provideGetFareArgumentsWithDistance")
    void getFareByDistance(int distance, int expectedFare) {
        // given
        Fare fare = Fare.of(DistanceFarePolicy.of(distance));

        // when
        int fareValue = fare.get();

        // then
        assertThat(fareValue).isEqualTo(expectedFare);
    }

    @DisplayName("추가 요금과 경로에 따른 요금을 계산한다.")
    @ParameterizedTest
    @MethodSource("provideGetFareArgumentsWithDistanceAndExtraCharge")
    void getFareByDistanceAndExtraCharge(int distance, int extraCharge, int expectedFare) {
        // given
        Fare fare = Fare.of(DistanceFarePolicy.of(distance));
        fare.add(ExtraChargeFarePolicy.of(extraCharge));

        // when
        int fareValue = fare.get();

        // then
        assertThat(fareValue).isEqualTo(expectedFare);
    }

    @DisplayName("나이에 따른 요금을 계산한다.")
    @ParameterizedTest
    @MethodSource("provideGetFareArgumentsWithAge")
    void getFareByAge(int age, int expectedFare) {
        // given
        Fare fare = Fare.of(DiscountFarePolicy.of(age));

        // when
        int fareValue = fare.get();

        // then
        assertThat(fareValue).isEqualTo(expectedFare);
    }

    public static Stream<Arguments> provideGetFareArgumentsWithDistance() {
        return Stream.of(
                Arguments.of(1, 1250),
                Arguments.of(10, 1250),
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
                Arguments.of(50, 2050),
                Arguments.of(51, 2150),
                Arguments.of(58, 2150),
                Arguments.of(59, 2250),
                Arguments.of(66, 2250),
                Arguments.of(67, 2350),
                Arguments.of(74, 2350)
        );
    }

    public static Stream<Arguments> provideGetFareArgumentsWithDistanceAndExtraCharge() {
        return Stream.of(
                Arguments.of(1, 0, 1250),
                Arguments.of(1, 100, 1350),
                Arguments.of(1, 1000, 2250),
                Arguments.of(10, 1000, 2250),
                Arguments.of(11, 1000, 2350),
                Arguments.of(50, 1000, 3050),
                Arguments.of(74, 1000, 3350)
        );
    }

    public static Stream<Arguments> provideGetFareArgumentsWithAge() {
        return Stream.of(
                Arguments.of(12, 800),
                Arguments.of(13, 1070),
                Arguments.of(18, 1070),
                Arguments.of(19, 1250)
        );
    }
}
