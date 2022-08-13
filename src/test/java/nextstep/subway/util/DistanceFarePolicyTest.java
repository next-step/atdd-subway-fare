package nextstep.subway.util;

import nextstep.subway.payment.DistanceFarePolicy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFarePolicyTest {

    @ParameterizedTest
    @MethodSource("distanceAndFare")
    void calculateNormalFareTest(int distance, int fare) {
        DistanceFarePolicy distanceFarePolicy = new DistanceFarePolicy();
        int calculatedFare = distanceFarePolicy.calculateFare(distance);
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