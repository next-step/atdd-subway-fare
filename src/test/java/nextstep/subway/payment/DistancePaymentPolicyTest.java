package nextstep.subway.payment;

import nextstep.subway.domain.Fare;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DistancePaymentPolicyTest {

    @ParameterizedTest
    @MethodSource("distanceAndFare")
    void calculateNormalFareTest(int distance, int expectedFare) {
        PaymentPolicy distanceFarePolicy = new DistancePaymentPolicy(distance);
        Fare fare = Fare.from(0);
        distanceFarePolicy.calculate(fare);
        assertThat(fare.fare()).isEqualTo(expectedFare);
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