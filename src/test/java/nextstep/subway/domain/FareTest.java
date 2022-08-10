package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FareTest {

    @DisplayName("기본 요금")
    @ParameterizedTest
    @MethodSource
    void standard(final int actual, final int expected) {
        assertFare(actual, expected);
    }

    private static Stream<Arguments> standard() {
        return Stream.of(
            Arguments.of(거리요금_계산(10), 1250),
            Arguments.of(거리요금_계산(9), 1250),
            Arguments.of(거리요금_계산(8), 1250),
            Arguments.of(거리요금_계산(7), 1250),
            Arguments.of(거리요금_계산(6), 1250)
        );
    }

    @DisplayName("10 킬로미터 이상 5km 마다 100원 + 기본요금")
    @ParameterizedTest
    @MethodSource
    void over10Kilometers(final int actual, final int distance) {
        assertFare(actual, distance);
    }

    private static Stream<Arguments> over10Kilometers() {
        return Stream.of(
            Arguments.of(거리요금_계산(11), 1350),
            Arguments.of(거리요금_계산(16), 1450),
            Arguments.of(거리요금_계산(21), 1550),
            Arguments.of(거리요금_계산(12), 1350),
            Arguments.of(거리요금_계산(26), 1650)
        );
    }

    @ParameterizedTest
    @DisplayName("50 킬로미터 이상 8km 마다 100원 + 기본요금")
    @MethodSource
    void over50Kilometers(final int actual, final int expected) {
        assertFare(actual, expected);
    }

    private static Stream<Arguments> over50Kilometers() {
        return Stream.of(
            Arguments.of(거리요금_계산(51), 1850),
            Arguments.of(거리요금_계산(59), 1950)
        );
    }

    private static void assertFare(final int actual, final int expected) {
        assertThat(actual).isEqualTo(expected);
    }

    private static int 거리요금_계산(final int distance) {
        return Fare.calculateOverFare(distance);
    }

}