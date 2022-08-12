package nextstep.subway.util;

import nextstep.subway.util.fare.NormalFarePolicy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class NormalFarePolicyTest {

    @ParameterizedTest
    @MethodSource("distanceAndFare")
    void calculateNormalFareTest(int distance, int fare) {
        NormalFarePolicy normalFarePolicy = new NormalFarePolicy();
        int calculatedFare = normalFarePolicy.calculateFare(distance);
        assertThat(calculatedFare).isEqualTo(fare);
    }

    private static Stream<Arguments> distanceAndFare() {
        return Stream.of(
                Arguments.of(10, 1250),
                Arguments.of(50, 2050),
                Arguments.of(51, 2150),
                Arguments.of(55, 2150),
                Arguments.of(59, 2250)
        );
    }

}