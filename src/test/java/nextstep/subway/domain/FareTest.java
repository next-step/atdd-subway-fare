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
    @MethodSource("provideGetFareArguments")
    void getFare(int distance, int expectedFare) {
        // when
        int fare = Fare.of(distance).get();

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    public static Stream<Arguments> provideGetFareArguments() {
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

}
